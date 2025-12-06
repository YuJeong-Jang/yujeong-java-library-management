package com.book.library.book.response;

import com.book.library.Enums;
import com.book.library.book.domain.Book;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class BookResponse {
    Long id;
    String title;
    String author;
    String isbn;
    String publisher;
    String category;
    Integer totalQuantity;
    Integer availableQty;
    Enums.BookStatus status;
    Instant createdAt;
    Instant updatedAt;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .category(book.getCategory())
                .totalQuantity(book.getTotalQuantity())
                .availableQty(book.getAvailableQty())
                .status(book.getStatus())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
