package com.book.library.rental.service;

import com.book.library.Enums;
import com.book.library.book.repository.BookRepository;
import com.book.library.common.exception.BusinessException;
import com.book.library.member.repository.MemberRepository;
import com.book.library.rental.domain.Rental;
import com.book.library.rental.dto.RentalCreateRequest;
import com.book.library.rental.dto.RentalResponse;
import com.book.library.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
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
    
    @Transactional
    public RentalResponse createRental(RentalCreateRequest request) {
        // 회원 확인
        if (!memberRepository.existsById(request.getMemberId())) {
            throw new BusinessException("회원을 찾을 수 없습니다. ID: " + request.getMemberId());
        }
        
        // 도서 확인 및 재고 확인
        var book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. ID: " + request.getBookId()));
        
        if (book.getAvailableQty() <= 0) {
            throw new BusinessException("대여 가능한 도서가 없습니다.");
        }
        
        // 대여 생성
        Rental rental = new Rental();
        rental.setMemberId(request.getMemberId());
        rental.setBookId(request.getBookId());
        rental.setRentalStatus(Enums.RentalStatus.RENTED);
        
        Instant now = Instant.now();
        rental.setRentalDate(now);
        
        // dueDate가 제공되면 사용, 아니면 14일 후로 설정
        if (request.getDueDate() != null) {
            rental.setDueDate(request.getDueDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        } else {
            rental.setDueDate(now.plus(14, ChronoUnit.DAYS));
        }
        
        rental.setRemarks(request.getRemarks());
        
        Rental saved = rentalRepository.save(rental);
        
        // 도서 재고 감소
        book.setAvailableQty(book.getAvailableQty() - 1);
        if (book.getAvailableQty() == 0) {
            book.setStatus(Enums.BookStatus.BORROWED);
        }
        bookRepository.save(book);
        
        return RentalResponse.from(saved);
    }
    
    @Transactional
    public RentalResponse returnBook(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new BusinessException("대여 기록을 찾을 수 없습니다. ID: " + rentalId));
        
        if (rental.getRentalStatus() == Enums.RentalStatus.RETURNED) {
            throw new BusinessException("이미 반납된 도서입니다.");
        }
        
        // 반납 처리 (RENTED 또는 OVERDUE 상태 모두 반납 가능)
        rental.setRentalStatus(Enums.RentalStatus.RETURNED);
        rental.setReturnDate(Instant.now());
        Rental saved = rentalRepository.save(rental);
        
        // 도서 재고 증가
        var book = bookRepository.findById(rental.getBookId())
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다."));
        book.setAvailableQty(book.getAvailableQty() + 1);
        book.setStatus(Enums.BookStatus.AVAILABLE);
        bookRepository.save(book);
        
        return RentalResponse.from(saved);
    }
    
    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new BusinessException("대여 기록을 찾을 수 없습니다. ID: " + id));
        
        // 대여 중인 경우 도서 재고 복구 (RENTED 또는 OVERDUE 상태)
        if (rental.getRentalStatus() == Enums.RentalStatus.RENTED || 
            rental.getRentalStatus() == Enums.RentalStatus.OVERDUE) {
            var book = bookRepository.findById(rental.getBookId())
                    .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다."));
            book.setAvailableQty(book.getAvailableQty() + 1);
            book.setStatus(Enums.BookStatus.AVAILABLE);
            bookRepository.save(book);
        }
        
        rentalRepository.deleteById(id);
    }
}