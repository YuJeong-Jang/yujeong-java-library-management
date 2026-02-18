package com.book.library.rental.config;

import com.book.library.common.config.CommonDataSourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
    "com.book.library.rental.service",
    "com.book.library.rental.repository",
    "com.book.library.rental.config",
    "com.book.library.member.repository",
    "com.book.library.book.repository"
})
@Import(CommonDataSourceConfig.class)
public class RootConfig {
}