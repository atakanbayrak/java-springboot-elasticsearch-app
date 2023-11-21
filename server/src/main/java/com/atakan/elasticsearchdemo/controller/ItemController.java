package com.atakan.elasticsearchdemo.controller;

import com.atakan.elasticsearchdemo.dto.SearchRequestDto;
import com.atakan.elasticsearchdemo.model.Item;
import com.atakan.elasticsearchdemo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item createIndex(@RequestBody Item item)
    {
        return itemService.createIndex(item);
    }

    @PostMapping("/init-index")
    public void addItemsFromJson()
    {
        itemService.addItemsFromJson();
    }

    @GetMapping("/getAllDataFromIndex/{indexName}")
    public List<Item> getAllDataFromIndex(@PathVariable String indexName)
    {
        return itemService.getAllDataFromIndex(indexName);
    }

    @GetMapping("/search")
    public List<Item> searchItemsByFieldAndValue(@RequestBody SearchRequestDto dto)
    {
        return itemService.searchItemsByFieldAndValue(dto);
    }

    @GetMapping("/search/{id}/{title}")
    public List<Item> searchItemsByIdAndTitleWithQuery(@PathVariable String id,
                                                         @PathVariable String title)
    {
        return itemService.searchItemsByIdAndTitleWithQuery(id,title);
    }

    @GetMapping("/boolQuery")
    public List<Item> boolQuery(@RequestBody SearchRequestDto dto)
    {
        return itemService.boolQuery(dto);
    }

    //Oto bulma ve ngram teknolojisi burada kullanılıyor
    @GetMapping("/autoSuggest/{title}")
    public List<String> autoSuggestItemsByTitle(@PathVariable String title)
    {
        //Set döndürülmesinin nedeni duplicate elemanlara izin verilmemesi, birden fazla aynı öneri istenmemesi.
        return itemService.findSuggestedItemTitles(title);
    }

    @GetMapping("/suggestionsQuery/{title}")
    public List<String> autoSuggestItemsByTitleWithQuery(@PathVariable String title)
    {
        return itemService.autoSuggestItemsByNameWithQuery(title);
    }

    @GetMapping("/fuzzyQuery/{title}")
    public List<String> autoSuggestItemsByTitleFuzziness(@PathVariable String title)
    {
        return itemService.autoSuggestItemsByNameFuzzinness(title);
    }
}
