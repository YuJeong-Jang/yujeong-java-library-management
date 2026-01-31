package com.book.library.frontend.controller;

import com.book.library.frontend.service.BookService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    
    private final BookService bookService;
    
    @GetMapping
    public String books(Model model, @RequestParam(required = false) String keyword, HttpSession session) {
        // 로그인 정보 추가
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                model.addAttribute("books", bookService.searchBooks(keyword));
                model.addAttribute("keyword", keyword);
            } else {
                model.addAttribute("books", bookService.getAllBooks());
            }
        } catch (Exception e) {
            model.addAttribute("books", java.util.Collections.emptyList());
            model.addAttribute("error", "도서 목록을 불러오는 중 오류가 발생했습니다.");
        }
        return "books/books";
    }
    
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createBook(@RequestBody Map<String, Object> bookData) {
        Map<String, Object> result = bookService.createBook(bookData);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable Long id, @RequestBody Map<String, Object> bookData) {
        Map<String, Object> result = bookService.updateBook(id, bookData);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteBook(@PathVariable Long id) {
        Map<String, Object> result = bookService.deleteBook(id);
        return ResponseEntity.ok(result);
    }
    
    // API 엔드포인트
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllBooksApi() {
        try {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", bookService.getAllBooks()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "도서 목록을 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBookApi(@PathVariable Long id) {
        try {
            Map<String, Object> book = bookService.getBook(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", book
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "도서 정보를 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
}