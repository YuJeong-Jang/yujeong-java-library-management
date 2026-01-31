package com.book.library.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.book.library")
public class LibraryMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryMemberApplication.class, args);
    }
}
