package com.atakan.elasticsearchdemo.repo;

import com.atakan.elasticsearchdemo.model.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends ElasticsearchRepository<Item,String> {

}
