package com.book.library.book.service;

import com.book.library.Enums;
import com.book.library.book.domain.Book;
import com.book.library.book.dto.BookCreateRequest;
import com.book.library.book.dto.BookUpdateRequest;
import com.book.library.book.repository.BookRepository;
import com.book.library.book.response.BookResponse;
import com.book.library.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    
    private final BookRepository bookRepository;
    
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }
    
    public BookResponse getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. ID: " + id));
        return BookResponse.from(book);
    }
    
    public List<BookResponse> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword)
                .stream()
                .map(BookResponse::from)
                .toList();
    }
    
    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublisher(request.getPublisher());
        book.setPublishDate(request.getPublishDate());
        book.setCategory(request.getCategory());
        
        // totalQuantity와 availableQty가 null이면 기본값 1 설정
        Integer totalQty = request.getTotalQuantity() != null ? request.getTotalQuantity() : 1;
        Integer availableQty = request.getAvailableQty() != null ? request.getAvailableQty() : totalQty;
        
        book.setTotalQuantity(totalQty);
        book.setAvailableQty(availableQty);
        book.setStatus(Enums.BookStatus.AVAILABLE);
        book.setCreatedAt(Instant.now());
        book.setUpdatedAt(Instant.now());
        
        Book saved = bookRepository.save(book);
        return BookResponse.from(saved);
    }
    
    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. ID: " + id));
        
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublisher(request.getPublisher());
        book.setPublishDate(request.getPublishDate());
        book.setCategory(request.getCategory());
        
        // totalQuantity와 availableQty가 null이 아닐 때만 업데이트
        if (request.getTotalQuantity() != null) {
            book.setTotalQuantity(request.getTotalQuantity());
        }
        if (request.getAvailableQty() != null) {
            book.setAvailableQty(request.getAvailableQty());
        }
        
        book.setUpdatedAt(Instant.now());
        
        Book saved = bookRepository.save(book);
        return BookResponse.from(saved);
    }
    
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BusinessException("도서를 찾을 수 없습니다. ID: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    @Transactional
    public void decreaseAvailableQuantity(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. ID: " + bookId));
        
        if (book.getAvailableQty() <= 0) {
            throw new BusinessException("대여 가능한 도서가 없습니다.");
        }
        
        book.setAvailableQty(book.getAvailableQty() - 1);
        if (book.getAvailableQty() == 0) {
            book.setStatus(Enums.BookStatus.BORROWED);
        }
        bookRepository.save(book);
    }
    
    @Transactional
    public void increaseAvailableQuantity(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException("도서를 찾을 수 없습니다. ID: " + bookId));
        
        book.setAvailableQty(book.getAvailableQty() + 1);
        book.setStatus(Enums.BookStatus.AVAILABLE);
        bookRepository.save(book);
    }
}