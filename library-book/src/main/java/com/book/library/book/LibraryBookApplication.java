package com.book.library.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.book.library")           // 공통 domain 전체
@EnableJpaRepositories("com.book.library") // 공통 repository 전체
public class LibraryBookApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryBookApplication.class, args);
    }
}
