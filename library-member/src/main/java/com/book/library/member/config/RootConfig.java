package com.book.library.member.config;

import com.book.library.common.config.CommonDataSourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
    "com.book.library.member.service",
    "com.book.library.member.repository",
    "com.book.library.member.config"
})
@Import(CommonDataSourceConfig.class)
public class RootConfig {
}