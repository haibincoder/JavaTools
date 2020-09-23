package com.bincoder.EsTools.service;

import com.bincoder.EsTools.config.EsConfig;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsTools {
    public static final String INDEX = "search";

    private RestClientBuilder restClientBuilder;
    private RestHighLevelClient restHighLevelClient;

    public EsTools(){
        EsConfig esConfig = new EsConfig();
        restClientBuilder = esConfig.restClientBuilder();
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
    }

    /**
     * 通过id获取数据
     */
    public GetResponse get(String id) throws IOException {
        GetRequest request = new GetRequest(INDEX, id);
        return restHighLevelClient.get(request, RequestOptions.DEFAULT);
    }

    /**
     * 删除document
     */
    public DeleteResponse delete(String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX);
        deleteRequest.id(id);
        return restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /**
     * 创建索引
     */
    public CreateIndexResponse createIndex(String index) throws IOException{
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 删除索引
     */
    public AcknowledgedResponse deleteIndex(String index) throws IOException{
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        return restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 插入json数据
     */
    public IndexResponse insertJson(String content) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.source(content, XContentType.JSON);
        // indexRequest.id("4");
        return restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 批量插入json数据
     */
    public BulkResponse insertBatchJson(List<String> contentList) throws IOException{
        BulkRequest bulkRequest = new BulkRequest();
        IndexRequest indexRequest;
        for(String item : contentList){
            indexRequest = new IndexRequest(INDEX);
            indexRequest.source(item, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse search(QueryBuilder queryBuilder) throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        request.source(searchSourceBuilder);

        return restHighLevelClient.search(request, RequestOptions.DEFAULT);

    }

    public static void main(String[] args) throws IOException {
        // 批量插入
        String content = "{\n" +
                "    \"search_id\":1,\n" +
                "    \"search_type\":\"task\",\n" +
                "    \"title\":\"支付宝时什么\",\n" +
                "    \"descript\":\"支付宝,全球领先的独立第三方支付平台,致力于为广大用户提供安全快速的电子支付/网上支付/安全支付/手机支付体验,及转账收款/水电煤缴费/信用卡还款/AA收款等生活...\"\n" +
                "}";
        EsTools esTools = new EsTools();
        List<String> contentList = new ArrayList<>();
        contentList.add(content);
        contentList.add(content);
        BulkResponse indexResponse = esTools.insertBatchJson(contentList);
        System.out.println(indexResponse.toString());

        String query = "支付宝";
        esTools = new EsTools();
        QueryBuilder search = esTools.searchBuild(query);
        SearchResponse response = esTools.search(search);
        System.out.println(response.toString());

        if(response.getHits().getHits() != null) {
            Map result = response.getHits().getHits()[0].getSourceAsMap();
            System.out.println(result.toString());
        }
    }

    private QueryBuilder searchBuild(String query){
        /**
         * {
         *   "query": {
         *     "bool": {
         *       "must": [
         *         {
         *           "multi_match":{
         *             "query":"支付宝",
         *             "fields": ["title", "descript"]
         *           }
         *         },
         *         {
         *           "term":{
         *             "search_type":"batch"
         *           }
         *         }
         *       ]
         *     }
         *   }
         * }
         */
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("search_type", "task");
        boolQueryBuilder.filter(termQueryBuilder);

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(query, "title", "descript");
        boolQueryBuilder.must(multiMatchQueryBuilder);
        return boolQueryBuilder;
    }
}
