package com.atakan.elasticsearchdemo.service;

import com.atakan.elasticsearchdemo.model.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonDataService {
    private final ObjectMapper objcetMapper;

    public List<Item> readItemsFromJson()
    {
        try
        {
            ClassPathResource resource = new ClassPathResource("data/items.json");
            InputStream inputStream = resource.getInputStream();
            return objcetMapper.readValue(inputStream, new TypeReference<List<Item>>() {
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }
}
