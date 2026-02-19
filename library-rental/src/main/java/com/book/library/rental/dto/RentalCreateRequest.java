package com.book.library.rental.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentalCreateRequest {
    private Long memberId;
    private Long bookId;
    private LocalDate dueDate; // 반납예정일
    private String remarks;
}