package com.atakan.elasticsearchdemo.controller;

import com.atakan.elasticsearchdemo.dto.SearchRequestDto;
import com.atakan.elasticsearchdemo.model.Item;
import com.atakan.elasticsearchdemo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}