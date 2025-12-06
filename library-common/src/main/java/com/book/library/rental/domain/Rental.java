package com.book.library.rental.domain;

import com.book.library.book.domain.Book;
import com.book.library.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

// rented = 0, returned = 1, overdue = 2
// rented : 대여, returned : 반납, overdue : 연체
enum RentalStatus { RENTED, RETURNED, OVERDUE }

@Entity
@Table(name = "rental")
@Data
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "rental_status", nullable = false)
    private RentalStatus rentalStatus = RentalStatus.RETURNED;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "rental_date", nullable = false)
    private Instant rentalDate;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Column(name = "return_date")
    private Instant returnDate;

    @Size(max = 255)
    @Column(name = "remarks")
    private String remarks;

}
