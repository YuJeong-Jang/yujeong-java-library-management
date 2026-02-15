package com.book.library.rental.dto;

import com.book.library.Enums;
import com.book.library.rental.domain.Rental;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponse {
    private Long id;
    private Long memberId;
    private String memberName;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private Enums.RentalStatus rentalStatus;
    private Instant rentalDate;
    private Instant dueDate;
    private Instant returnDate;
    private String remarks;
    
    public static RentalResponse from(Rental rental) {
        return new RentalResponse(
            rental.getId(),
            rental.getMemberId(),
            null, // memberName - JDBC에서는 조인 필요
            rental.getBookId(),
            null, // bookTitle - JDBC에서는 조인 필요
            null, // bookAuthor - JDBC에서는 조인 필요
            rental.getRentalStatus(),
            rental.getRentalDate(),
            rental.getDueDate(),
            rental.getReturnDate(),
            rental.getRemarks()
        );
    }
}