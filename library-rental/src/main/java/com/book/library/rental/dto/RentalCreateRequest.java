package com.book.library.rental.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalCreateRequest {
    @NotNull(message = "회원 ID는 필수입니다")
    private Long memberId;
    
    @NotNull(message = "도서 ID는 필수입니다")
    private Long bookId;
    
    private String remarks;
    
    @Override
    public String toString() {
        return "RentalCreateRequest{" +
                "memberId=" + memberId +
                ", bookId=" + bookId +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}