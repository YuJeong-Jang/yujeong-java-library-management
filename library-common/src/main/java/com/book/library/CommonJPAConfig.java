package com.book.library;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.book.library")           // 모든 domain 스캔
@EnableJpaRepositories(basePackages = "com.book.library") // 모든 repository 스캔
public class CommonJPAConfig {
}

