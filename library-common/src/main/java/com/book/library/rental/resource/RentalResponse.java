package com.book.library.rental.resource;

import com.book.library.Enums;
import com.book.library.rental.domain.Rental;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class RentalResponse {
    Long id;
    Long memberId;
    String memberName;
    Long bookId;
    String bookTitle;
    Enums.RentalStatus rentalStatus;
    Instant rentalDate;
    Instant dueDate;
    Instant returnDate;
    String remarks;

    public static RentalResponse from(Rental rental) {
        return RentalResponse.builder()
                .id(rental.getId())
                .memberId(rental.getMemberId())
                .memberName(null) // JDBC에서는 조인 필요
                .bookId(rental.getBookId())
                .bookTitle(null) // JDBC에서는 조인 필요
                .rentalStatus(rental.getRentalStatus())
                .rentalDate(rental.getRentalDate())
                .dueDate(rental.getDueDate())
                .returnDate(rental.getReturnDate())
                .remarks(rental.getRemarks())
                .build();
    }
}