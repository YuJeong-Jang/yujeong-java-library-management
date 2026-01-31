package com.book.library.rental.repository;

import com.book.library.Enums;
import com.book.library.rental.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.member.id = :memberId")
    List<Rental> findByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId")
    List<Rental> findByBookId(@Param("bookId") Long bookId);
    
    @Query("SELECT r FROM Rental r WHERE r.rentalStatus = :status")
    List<Rental> findByRentalStatus(@Param("status") Enums.RentalStatus status);
    
    @Query("SELECT r FROM Rental r WHERE r.rentalStatus = :status AND r.dueDate < :now")
    List<Rental> findOverdueRentals(@Param("status") Enums.RentalStatus status, @Param("now") Instant now);
    
    @Query("SELECT r FROM Rental r WHERE r.member.id = :memberId AND r.rentalStatus = :status")
    List<Rental> findByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") Enums.RentalStatus status);
}
