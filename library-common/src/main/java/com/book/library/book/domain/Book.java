package com.book.library.book.domain;

import com.book.library.Enums;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long id;

    @Size(max = 20)
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Size(max = 200)
    @NotNull
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 100)
    @NotNull
    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Size(max = 100)
    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Size(max = 100)
    @Column(name = "category", length = 100)
    private String category;

    @ColumnDefault("1")
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity = 1;

    @ColumnDefault("1")
    @Column(name = "available_qty", nullable = false)
    private Integer availableQty = 1;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private Enums.BookStatus status = Enums.BookStatus.AVAILABLE;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

}
