package com.book.library.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.book.library")
public class LibraryBookApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryBookApplication.class, args);
    }
}
