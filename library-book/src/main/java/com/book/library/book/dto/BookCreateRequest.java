package com.book.library.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookCreateRequest {
    @NotBlank(message = "제목은 필수입니다")
    private String title;
    
    @NotBlank(message = "저자는 필수입니다")
    private String author;
    
    private String isbn;
    private String publisher;
    private LocalDate publishDate;
    private String category;
    
    @NotNull(message = "총 수량은 필수입니다")
    @Positive(message = "총 수량은 양수여야 합니다")
    private Integer totalQuantity;
    
    @NotNull(message = "이용 가능 수량은 필수입니다")
    @Positive(message = "이용 가능 수량은 양수여야 합니다")
    private Integer availableQty;
}