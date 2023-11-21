package com.atakan.elasticsearchdemo.Util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.termvectors.Term;
import co.elastic.clients.util.ObjectBuilder;
import com.atakan.elasticsearchdemo.dto.SearchRequestDto;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class EsUtil {
    public static Query createMatchAllQuery()
    {
        return Query.of(q -> q.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForFieldAndValue(String fieldName, String searchValue)
    {
        return() -> Query.of(q -> q.match(buildMatchQueryForFieldAndValue(fieldName,searchValue)));
    }

    private static MatchQuery buildMatchQueryForFieldAndValue(String fieldName, String searchValue)
    {
        return new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build();
    }

    public static Supplier<Query> createBoolQuery(SearchRequestDto dto)
    {
        return () -> Query.of(q -> q.bool(boolQuery(dto.getFieldName().get(0), dto.getSearchValue().get(0),
                dto.getFieldName().get(1), dto.getSearchValue().get(1))));
    }

    private static BoolQuery boolQuery(String key1, String value1, String key2, String value2)
    {
        return new BoolQuery.Builder()
                .filter(termQuery(key1,value1))
                .must(matchQuery(key2,value2))
                .build();
    }

    private static Query matchQuery(String field, String value)
    {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field(field)
                .query(value)
                .build()));
    }

    private static Query termQuery(String field, String value)
    {
        return Query.of(q -> q.term(new TermQuery.Builder()
                .field(field)
                .value(value)
                .build()));
    }

    public static Query buildAutoSuggestQuery(String title)
    {
        return Query.of(q -> q.match(new MatchQuery.Builder()
                .field("title")
                .query(title)
                .analyzer("custom_index")
                .build()));
    }
}
