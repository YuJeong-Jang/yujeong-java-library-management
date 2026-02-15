package com.book.library.rental.domain;

import com.book.library.Enums;
import lombok.Data;

import java.time.Instant;

@Data
public class Rental {
    private Long id;
    private Long memberId;
    private Long bookId;
    private Enums.RentalStatus rentalStatus = Enums.RentalStatus.RENTED;
    private Instant rentalDate;
    private Instant dueDate;
    private Instant returnDate;
    private String remarks;
}
