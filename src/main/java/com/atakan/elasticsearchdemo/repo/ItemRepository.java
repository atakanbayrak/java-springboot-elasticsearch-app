package com.atakan.elasticsearchdemo.repo;

import com.atakan.elasticsearchdemo.model.Item;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"id\": \"?0\"}}, {\"match\": {\"title\": \"?1\"}}]}}")
    List<Item> searchByIdAndTitle(String id, String title);
}
