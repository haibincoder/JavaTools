package com.bincoder.EsTools.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;

/**
 * ES builder
 */
public class EsConfig {
    /**
     * es restful client builder
     * @return restful client
     */
    @Bean
    public RestClientBuilder restClientBuilder(){
        // 设置IP
        HttpHost esHost = new HttpHost("localhost", 9200);

        RestClientBuilder restClientBuilder = RestClient.builder(esHost);
        // setPassword(restClientBuilder);
        // setTImeout(restClientBuilder);

        return restClientBuilder;
    }

    /**
     * 设置超时时间
     */
    private void setTImeout(RestClientBuilder restClientBuilder) {
        restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                return builder.setConnectTimeout(1000)
                        .setSocketTimeout(1000);
            }
        });
    }

    /**
     * 设置ES密码
     */
    private void setPassword(RestClientBuilder restClientBuilder) {
        // 设置密码
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("userName", "password"));


        restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setDefaultIOReactorConfig(
                                IOReactorConfig.custom()
                                .setIoThreadCount(4)
                                .build()
                        );
            }
        });
    }
}
