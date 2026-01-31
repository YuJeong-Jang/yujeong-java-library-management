package com.book.library.rental.service;

import com.book.library.Enums;
import com.book.library.book.domain.Book;
import com.book.library.book.repository.BookRepository;
import com.book.library.common.exception.BusinessException;
import com.book.library.member.domain.Member;
import com.book.library.member.repository.MemberRepository;
import com.book.library.rental.domain.Rental;
import com.book.library.rental.dto.RentalCreateRequest;
import com.book.library.rental.dto.RentalResponse;
import com.book.library.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentalService {
    
    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    
    public List<RentalResponse> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(RentalResponse::from)
                .toList();
    }
    
    public Page<RentalResponse> getRentals(Pageable pageable) {
        return rentalRepository.findAll(pageable)
                .map(RentalResponse::from);
    }
    
    public RentalResponse getRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("대여 기록을 찾을 수 없습니다. ID: " + id));
        return RentalResponse.from(rental);
    }
    
    public List<RentalResponse> getRentalsByMember(Long memberId) {
        return rentalRepository.findByMemberId(memberId).stream()
                .map(RentalResponse::from)
                .toList();
    }
    
    public List<RentalResponse> getRentalsByBook(Long bookId) {
        return rentalRepository.findByBookId(bookId).stream()
                .map(RentalResponse::from)
                .toList();
    }
    
    public List<RentalResponse> getActiveRentals() {
        return rentalRepository.findByRentalStatus(Enums.RentalStatus.RENTED).stream()
                .map(RentalResponse::from)
                .toList();
    }
    
    public List<RentalResponse> getOverdueRentals() {
        Instant now = Instant.now();
        return rentalRepository.findOverdueRentals(Enums.RentalStatus.RENTED, now).stream()
                .map(RentalResponse::from)
                .toList();
    }
    
    @Transactional
    public RentalResponse createRental(RentalCreateRequest request) {
        try {
            // 입력 검증
            if (request.getMemberId() == null) {
                throw new BusinessException("회원 ID가 필요합니다.");
            }
            if (request.getBookId() == null) {
                throw new BusinessException("도서 ID가 필요합니다.");
            }
            
            // 회원 확인
            Member member = memberRepository.findById(request.getMemberId())
                    .orElseThrow(() -> new BusinessException("회원을 찾을 수 없습니다. ID: " + request.getMemberId()));
            
            if (member.getStatus() != Enums.MemberStatus.ACTIVE) {
                throw new BusinessException("비활성화된 회원은 도서를 대여할 수 없습니다.");
            }
            
            // 도서 확인
            Book book = bookRepository.findById(request.getBookId())
                    .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. ID: " + request.getBookId()));
            
            if (book.getAvailableQty() == null || book.getAvailableQty() <= 0) {
                throw new BusinessException("대여 가능한 도서가 없습니다.");
            }
            
            // 이미 대여 중인지 확인
            List<Rental> activeRentals = rentalRepository.findByMemberIdAndStatus(request.getMemberId(), Enums.RentalStatus.RENTED);
            
            boolean alreadyRented = activeRentals.stream()
                    .anyMatch(rental -> rental.getBook().getId().equals(request.getBookId()));
            
            if (alreadyRented) {
                throw new BusinessException("이미 대여 중인 도서입니다.");
            }
            
            // 대여 생성
            Rental rental = new Rental();
            rental.setMember(member);
            rental.setBook(book);
            rental.setRentalStatus(Enums.RentalStatus.RENTED);
            
            Instant now = Instant.now();
            rental.setRentalDate(now);
            rental.setDueDate(now.plus(14, ChronoUnit.DAYS)); // 2주 후
            rental.setRemarks(request.getRemarks() != null ? request.getRemarks() : "");
            
            Rental saved = rentalRepository.save(rental);
            
            // 도서 재고 감소
            book.setAvailableQty(book.getAvailableQty() - 1);
            if (book.getAvailableQty() == 0) {
                book.setStatus(Enums.BookStatus.BORROWED);
            }
            bookRepository.save(book);
            
            return RentalResponse.from(saved);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("대여 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Transactional
    public RentalResponse returnBook(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new BusinessException("대여 기록을 찾을 수 없습니다. ID: " + rentalId));
        
        if (rental.getRentalStatus() != Enums.RentalStatus.RENTED) {
            throw new BusinessException("이미 반납된 도서입니다.");
        }
        
        // 반납 처리
        rental.setRentalStatus(Enums.RentalStatus.RETURNED);
        rental.setReturnDate(Instant.now());
        
        Rental saved = rentalRepository.save(rental);
        
        // 도서 재고 증가
        Book book = rental.getBook();
        book.setAvailableQty(book.getAvailableQty() + 1);
        book.setStatus(Enums.BookStatus.AVAILABLE);
        bookRepository.save(book);
        
        return RentalResponse.from(saved);
    }
    
    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("대여 기록을 찾을 수 없습니다. ID: " + id));
        
        // 대여 중인 경우 도서 재고 복구
        if (rental.getRentalStatus() == Enums.RentalStatus.RENTED || 
            rental.getRentalStatus() == Enums.RentalStatus.OVERDUE) {
            Book book = rental.getBook();
            book.setAvailableQty(book.getAvailableQty() + 1);
            book.setStatus(Enums.BookStatus.AVAILABLE);
            bookRepository.save(book);
        }
        
        rentalRepository.deleteById(id);
    }
    
    @Transactional
    public void updateOverdueStatus() {
        Instant now = Instant.now();
        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(Enums.RentalStatus.RENTED, now);
        
        for (Rental rental : overdueRentals) {
            rental.setRentalStatus(Enums.RentalStatus.OVERDUE);
        }
        
        rentalRepository.saveAll(overdueRentals);
    }
}