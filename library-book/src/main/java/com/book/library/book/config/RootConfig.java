package com.book.library.book.config;

import com.book.library.common.config.CommonDataSourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
    "com.book.library.book.service",
    "com.book.library.book.repository",
    "com.book.library.book.config"
})
@Import(CommonDataSourceConfig.class)
public class RootConfig {
}