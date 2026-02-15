package com.book.library.frontend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {
    "com.book.library.frontend.service",
    "com.book.library.frontend.config"
})
@PropertySource("classpath:application.properties")
public class RootConfig {
    // Root configuration for services and other non-web components
}