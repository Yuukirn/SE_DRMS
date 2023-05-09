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
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;


import java.io.IOException;

public class ESOperations {

    public void ESDocInsert(Example example) throws IOException {
        IndexRequest request=new IndexRequest("example");
        ObjectMapper objectMapper=new ObjectMapper();
        String user_json=objectMapper.writeValueAsString(example);
        request.source(user_json, XContentType.JSON );
        IndexResponse response=client.index(request, RequestOptions.DEFAULT);
    }


    public String ESDocSearch(String id) throws IOException {
        GetRequest request=new GetRequest().index("example").id(id);
        GetResponse response=client.get(request,RequestOptions.DEFAULT);
        return response.getSourceAsString();
    }


    public SearchHits ESDocFuzzyQuery(String name, String value) throws IOException {
        FuzzyQueryBuilder fuzzyQueryBuilder= QueryBuilders.fuzzyQuery(name,value).fuzziness(Fuzziness.TWO);

        NativeSearchQuery nativeSearchQuery=new NativeSearchQueryBuilder().withQuery(fuzzyQueryBuilder).build();

        SearchRequest request=new SearchRequest("example").source(new SearchSourceBuilder().query(fuzzyQueryBuilder));

        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

        return searchResponse.getHits();
    }



    private RestHighLevelClient client= new RestHighLevelClient(RestClient.builder
            (new HttpHost("115.156.206.51",9200,"https")));
}
