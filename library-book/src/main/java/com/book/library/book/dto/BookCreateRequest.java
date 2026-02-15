package com.book.library.book.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookCreateRequest {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private LocalDate publishDate;
    private String category;
    private Integer totalQuantity;
    private Integer availableQty;
}