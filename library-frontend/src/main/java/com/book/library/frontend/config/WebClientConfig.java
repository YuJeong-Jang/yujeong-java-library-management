package com.book.library.frontend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    
    @Value("${library.book.url}")
    private String bookUrl;
    
    @Value("${library.member.url}")
    private String memberUrl;
    
    @Value("${library.rental.url}")
    private String rentalUrl;
    
    @Bean("bookServiceWebClient")
    public WebClient bookServiceClient() {
        return WebClient.builder()
                .baseUrl(bookUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }
    
    @Bean("memberServiceWebClient")
    public WebClient memberServiceClient() {
        return WebClient.builder()
                .baseUrl(memberUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }
    
    @Bean("rentalServiceWebClient")
    public WebClient rentalServiceClient() {
        return WebClient.builder()
                .baseUrl(rentalUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }
}