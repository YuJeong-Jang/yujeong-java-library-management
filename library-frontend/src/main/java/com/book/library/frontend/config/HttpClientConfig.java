package com.book.library.frontend.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class HttpClientConfig {
    
    @Value("${library.book.url}")
    private String bookUrl;
    
    @Value("${library.member.url}")
    private String memberUrl;
    
    @Value("${library.rental.url}")
    private String rentalUrl;
    
    @Bean
    public CloseableHttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(10);
        
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    @Bean("bookServiceUrl")
    public String bookServiceUrl() {
        return bookUrl;
    }
    
    @Bean("memberServiceUrl")
    public String memberServiceUrl() {
        return memberUrl;
    }
    
    @Bean("rentalServiceUrl")
    public String rentalServiceUrl() {
        return rentalUrl;
    }
}