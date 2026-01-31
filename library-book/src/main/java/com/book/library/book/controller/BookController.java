package com.book.library.book.controller;

import com.book.library.book.dto.BookCreateRequest;
import com.book.library.book.dto.BookUpdateRequest;
import com.book.library.book.response.BookResponse;
import com.book.library.book.service.BookService;
import com.book.library.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(ApiResponse.success(books));
    }
    
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getBooks(Pageable pageable) {
        Page<BookResponse> books = bookService.getBooks(pageable);
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBook(@PathVariable Long id) {
        BookResponse book = bookService.getBook(id);
        return ResponseEntity.ok(ApiResponse.success(book));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookResponse>>> searchBooks(@RequestParam String keyword) {
        List<BookResponse> books = bookService.searchBooks(keyword);
        return ResponseEntity.ok(ApiResponse.success(books));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody BookCreateRequest request) {
        BookResponse book = bookService.createBook(request);
        return ResponseEntity
                .created(URI.create("/api/books/" + book.getId()))
                .body(ApiResponse.success("도서가 성공적으로 등록되었습니다.", book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request
    ) {
        BookResponse book = bookService.updateBook(id, request);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 수정되었습니다.", book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 삭제되었습니다.", null));
    }
}
