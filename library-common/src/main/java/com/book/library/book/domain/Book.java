package com.book.library.book.domain;

import com.book.library.Enums;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private String category;
    private Integer totalQuantity = 1;
    private Integer availableQty = 1;
    private Enums.BookStatus status = Enums.BookStatus.AVAILABLE;
    private Instant createdAt;
    private Instant updatedAt;
}
