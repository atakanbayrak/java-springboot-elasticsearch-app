package com.atakan.elasticsearchdemo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.atakan.elasticsearchdemo.Util.EsUtil;
import com.atakan.elasticsearchdemo.dto.SearchRequestDto;
import com.atakan.elasticsearchdemo.model.Item;
import com.atakan.elasticsearchdemo.repo.ItemRepository;
import com.atakan.elasticsearchdemo.service.JsonDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final JsonDataService jsonDataService;
    private final ElasticsearchClient elasticsearchClient;

    public Item createIndex(Item item)
    {
        return itemRepository.save(item);
    }

    public void addItemsFromJson()
    {
        log.info("Adding item from json");
        List<Item> itemList = jsonDataService.readItemsFromJson();
        itemRepository.saveAll(itemList);
    }

    public List<Item> getAllDataFromIndex(String indexName)
    {
        var query = EsUtil.createMatchAllQuery();
        log.info("Elastic Search Query {}", query.toString());
        SearchResponse<Item> response = null;

        try {
            response = elasticsearchClient.search(
                    q -> q.index(indexName).query(query), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Elastic Search Response {}", response);;
        return extractItemsFromResponse(response);
    }

    public List<Item> extractItemsFromResponse(SearchResponse<Item> response)
    {
        return response
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public Set<String> extractItemsFromAutoSuggest(SearchResponse<Item> response)
    {
        return response.hits()
            .hits()
            .stream()
            .map(Hit::source)
            .map(Item::getTitle)
            .collect(Collectors.toSet());
    }

    public List<Item> searchItemsByFieldAndValue(SearchRequestDto dto)
    {
        Supplier<Query> query = EsUtil.buildQueryForFieldAndValue(dto.getFieldName().get(0),
                dto.getSearchValue().get(0));
        log.info("ElasticSearch query {}", query.toString());
        SearchResponse<Item> response = null;

        try {
            response = elasticsearchClient.search(q -> q.index("questions_index").query(query.get()), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Elastic Search Response {}", response.toString());
        return extractItemsFromResponse(response);
    }

    public List<Item> searchItemsByIdAndTitleWithQuery(String id, String title)
    {
        return itemRepository.searchByIdAndTitle(id,title);
    }

    public List<Item> boolQuery(SearchRequestDto dto)
    {
        var query = EsUtil.createBoolQuery(dto);
        log.info("ElasticSearch query {}", query.toString());
        SearchResponse<Item> response = null;

        try {
            response = elasticsearchClient.search(q -> q.index("questions_index").query(query.get()), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("ElasticSearch query {}", response.toString());
        return extractItemsFromResponse(response);
    }

    public List<String> findSuggestedItemTitles(String title)
    {
        Query query = EsUtil.buildAutoSuggestQuery(title);
        log.info("ElasticSearch query {}", query.toString());
        SearchResponse<Item> response = null;
        try {
            return elasticsearchClient.search(q -> q.index("questions_index").query(query), Item.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .map(Item::getTitle)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //log.info("Elastic Search Response {}", response.toString());
        //return extractItemsFromAutoSuggest(response);
    }

    public List<String> autoSuggestItemsByNameWithQuery(String title)
    {
        List<Item> itemList = itemRepository.customAutoSuggest(title);
        log.info("Elastic Search Response: {}", itemList.toString());
        return itemList
                .stream()
                .map(Item::getTitle)
                .collect(Collectors.toList());
    }

    public List<String> autoSuggestItemsByNameFuzzinness(String title)
    {
        List<Item> itemList = itemRepository.fuzzyAutoSuggest(title);
        log.info("Elastic Search Response: {}", itemList.toString());
        return itemList
                .stream()
                .map(Item::getTitle)
                .collect(Collectors.toList());
    }
}
