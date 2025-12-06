package com.book.library.book.controller;

import com.book.library.book.domain.Book;
import com.book.library.book.repository.BookRepository;
import com.book.library.book.response.BookResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/library/books")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;

    // 1) 전체 목록 조회 (GET /library/books)
    @GetMapping
    public List<BookResponse> getBooks() {
        return bookRepository.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }

    // 2) 단건 조회 (GET /library/books/{id})
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(BookResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3) 생성 (POST /library/books)
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody Book request) {
        Book saved = bookRepository.save(request);
        return ResponseEntity
                .created(URI.create("/library/books/" + saved.getId()))
                .body(BookResponse.from(saved));
    }

    // 4) 수정 (PUT /library/books/{id} 전체 수정)
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Book request
    ) {
        return bookRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(request.getTitle());
                    existing.setAuthor(request.getAuthor());
                    existing.setIsbn(request.getIsbn());
                    existing.setCategory(request.getCategory());
                    existing.setTotalQuantity(request.getTotalQuantity());
                    existing.setAvailableQty(request.getAvailableQty());
                    existing.setStatus(request.getStatus());
                    Book saved = bookRepository.save(existing);
                    return ResponseEntity.ok(BookResponse.from(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 5) 삭제 (DELETE /library/books/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
