package com.nqff.drms.Algorithm;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.nqff.drms.pojo.Example;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;


import java.io.IOException;

public class ESOperations {

    public int ESDocInsert(Example example) throws IOException {
        IndexRequest request=new IndexRequest("example");
        ObjectMapper objectMapper=new ObjectMapper();
        String user_json=objectMapper.writeValueAsString(example);
        request.source(user_json, XContentType.JSON );
        IndexResponse response=client.index(request, RequestOptions.DEFAULT);

        return response.status().getStatus();
    }


    public String ESDocSearch(String id) throws IOException {
        GetRequest request=new GetRequest().index("example").id(id);
        GetResponse response=client.get(request,RequestOptions.DEFAULT);
        return response.getSourceAsString();
    }


    public String ESDocHighFuzzyQuery(String name, String value) throws IOException {
        FuzzyQueryBuilder fuzzyQueryBuilder= QueryBuilders.fuzzyQuery(name,value).fuzziness(Fuzziness.AUTO);

        NativeSearchQuery nativeSearchQuery=new NativeSearchQueryBuilder().withQuery(fuzzyQueryBuilder).build();

        SearchRequest request=new SearchRequest("example").source(new SearchSourceBuilder().query(fuzzyQueryBuilder));

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        return searchResponse.getHits().toString();
    }

    public String ESDocLowFuzzyQuery(String query) throws IOException {
        String type = "_doc";
        String index = "example";
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.types(type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("description", query));
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response=client.search(searchRequest,RequestOptions.DEFAULT);

        return response.toString();
    }



    private RestHighLevelClient client= new RestHighLevelClient(RestClient.builder
            (new HttpHost("115.156.206.51",9200,"https")));
}
