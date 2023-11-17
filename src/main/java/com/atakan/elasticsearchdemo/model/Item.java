package com.atakan.elasticsearchdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@Document(indexName = "questions_index")
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @Id
    private String id;
    @Field(name="title", type = FieldType.Text)
    private String title;
    @Field(name="content", type = FieldType.Text)
    private String content;
    @Field(name="category", type = FieldType.Text)
    private String category;
}
