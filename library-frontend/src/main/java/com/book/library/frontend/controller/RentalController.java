package com.book.library.frontend.controller;

import com.book.library.frontend.service.BookService;
import com.book.library.frontend.service.MemberService;
import com.book.library.frontend.service.RentalService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    
    private final RentalService rentalService;
    private final MemberService memberService;
    private final BookService bookService;
    
    @GetMapping
    public String rentals(Model model, HttpSession session) {
        // 로그인 정보 추가
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        
        try {
            List<Map<String, Object>> rentals = rentalService.getAllRentals();
            
            // 회원 및 도서 정보 조인
            List<Map<String, Object>> members = memberService.getAllMembers();
            List<Map<String, Object>> books = bookService.getAllBooks();
            
            // 각 rental에 member와 book 정보 추가
            for (Map<String, Object> rental : rentals) {
                Long memberId = getLongValue(rental.get("memberId"));
                Long bookId = getLongValue(rental.get("bookId"));
                
                // 회원 정보 찾기
                members.stream()
                    .filter(m -> memberId.equals(getLongValue(m.get("id"))))
                    .findFirst()
                    .ifPresent(member -> {
                        rental.put("memberName", member.get("name"));
                        rental.put("memberEmail", member.get("email"));
                    });
                
                // 도서 정보 찾기
                books.stream()
                    .filter(b -> bookId.equals(getLongValue(b.get("id"))))
                    .findFirst()
                    .ifPresent(book -> {
                        rental.put("bookTitle", book.get("title"));
                        rental.put("bookAuthor", book.get("author"));
                    });
            }
            
            model.addAttribute("rentals", rentals);
            
            // 대여 등록을 위한 도서 및 회원 목록 추가
            List<Map<String, Object>> allBooks = bookService.getAllBooks();
            List<Map<String, Object>> availableBooks = allBooks.stream()
                .filter(book -> {
                    Object status = book.get("status");
                    Object availableQty = book.get("availableQty");
                    boolean isAvailable = "AVAILABLE".equals(status) || Integer.valueOf(0).equals(status);
                    boolean hasQuantity = availableQty != null && 
                        (availableQty instanceof Integer ? (Integer) availableQty > 0 : 
                         Integer.parseInt(availableQty.toString()) > 0);
                    return isAvailable && hasQuantity;
                })
                .toList();
            model.addAttribute("availableBooks", availableBooks);
            
            List<Map<String, Object>> activeMembers = memberService.getActiveMembers();
            model.addAttribute("activeMembers", activeMembers);
            
            // 통계 계산
            long totalRentals = rentals.size();
            long activeRentals = rentals.stream()
                .filter(rental -> {
                    Object status = rental.get("rentalStatus");
                    return "RENTED".equals(status) || "OVERDUE".equals(status) || 
                           Integer.valueOf(0).equals(status) || Integer.valueOf(2).equals(status);
                })
                .count();
            long completedRentals = rentals.stream()
                .filter(rental -> {
                    Object status = rental.get("rentalStatus");
                    return "RETURNED".equals(status) || Integer.valueOf(1).equals(status);
                })
                .count();
            long overdueRentals = rentals.stream()
                .filter(rental -> {
                    Object status = rental.get("rentalStatus");
                    return "OVERDUE".equals(status) || Integer.valueOf(2).equals(status);
                })
                .count();
            
            model.addAttribute("totalRentals", totalRentals);
            model.addAttribute("activeRentals", activeRentals);
            model.addAttribute("completedRentals", completedRentals);
            model.addAttribute("overdueRentals", overdueRentals);
            
        } catch (Exception e) {
            model.addAttribute("rentals", java.util.Collections.emptyList());
            model.addAttribute("availableBooks", java.util.Collections.emptyList());
            model.addAttribute("activeMembers", java.util.Collections.emptyList());
            model.addAttribute("error", "대여 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("totalRentals", 0);
            model.addAttribute("activeRentals", 0);
            model.addAttribute("completedRentals", 0);
            model.addAttribute("overdueRentals", 0);
        }
        return "rentals/rentals";
    }
    
    private Long getLongValue(Object value) {
        if (value == null) return 0L;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    // 테스트 엔드포인트 - 백엔드 연결 확인
    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testRentalService() {
        Map<String, Object> result = rentalService.testConnection();
        return ResponseEntity.ok(result);
    }
    
    @PostMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createRental(@RequestBody Map<String, Object> rentalData) {
        Map<String, Object> result = rentalService.createRental(rentalData);
        return ResponseEntity.ok(result);
    }
    
    @PatchMapping("/{id}/return")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> returnBook(@PathVariable Long id) {
        Map<String, Object> result = rentalService.returnBook(id);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRental(@PathVariable Long id) {
        Map<String, Object> result = rentalService.deleteRental(id);
        return ResponseEntity.ok(result);
    }
    
    // API 엔드포인트 - 대여 목록
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllRentalsApi() {
        try {
            List<Map<String, Object>> rentals = rentalService.getAllRentals();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", rentals
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "대여 목록을 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
    
    // API 엔드포인트 - 회원 목록 (대여 등록용 - 활성 회원만)
    @GetMapping("/api/members")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMembersForRental() {
        try {
            List<Map<String, Object>> members = memberService.getActiveMembers();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", members
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "회원 목록을 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
    
    // API 엔드포인트 - 도서 목록
    @GetMapping("/api/books")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBooksForRental() {
        try {
            List<Map<String, Object>> books = bookService.getAllBooks();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", books
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "도서 목록을 불러오는 중 오류가 발생했습니다."
            ));
        }
    }
}