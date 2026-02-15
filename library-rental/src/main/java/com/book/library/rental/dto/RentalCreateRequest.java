package com.book.library.rental.dto;

import lombok.Data;

@Data
public class RentalCreateRequest {
    private Long memberId;
    private Long bookId;
    private String remarks;
}