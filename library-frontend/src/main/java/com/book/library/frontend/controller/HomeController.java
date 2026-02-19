package com.book.library.frontend.controller;

import com.book.library.frontend.service.AuthService;
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
@RequiredArgsConstructor
public class HomeController {
    
    private final AuthService authService;
    private final BookService bookService;
    private final MemberService memberService;
    private final RentalService rentalService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session, @RequestParam(required = false) String error) {
        // 로그인 정보 추가
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        
        try {
            // 통계 데이터 수집
            List<Map<String, Object>> books = bookService.getAllBooks();
            List<Map<String, Object>> members = memberService.getAllMembers();
            List<Map<String, Object>> rentals = rentalService.getAllRentals();
            
            // 도서 통계 - 총 권수 계산
            int totalBookQuantity = books.stream()
                .mapToInt(book -> {
                    Object qty = book.get("totalQuantity");
                    if (qty instanceof Integer) {
                        return (Integer) qty;
                    } else if (qty != null) {
                        try {
                            return Integer.parseInt(qty.toString());
                        } catch (NumberFormatException e) {
                            return 1;
                        }
                    }
                    return 1;
                })
                .sum();
            model.addAttribute("totalBooks", totalBookQuantity);
            
            // 회원 통계
            model.addAttribute("totalMembers", members.size());
            
            // 대여 통계 - rentalStatus 필드 확인
            long activeRentals = rentals.stream()
                .filter(rental -> {
                    Object status = rental.get("rentalStatus");
                    if (status == null) return false;
                    // Enum 문자열 또는 ordinal 값 체크
                    return "RENTED".equals(status.toString()) || Integer.valueOf(0).equals(status);
                })
                .count();
            long overdueRentals = rentals.stream()
                .filter(rental -> {
                    Object status = rental.get("rentalStatus");
                    if (status == null) return false;
                    // Enum 문자열 또는 ordinal 값 체크
                    return "OVERDUE".equals(status.toString()) || Integer.valueOf(2).equals(status);
                })
                .count();
            
            model.addAttribute("activeRentals", activeRentals);
            model.addAttribute("overdueRentals", overdueRentals);
            
        } catch (Exception e) {
            // 오류 발생 시 기본값 설정
            model.addAttribute("totalBooks", 0);
            model.addAttribute("totalMembers", 0);
            model.addAttribute("activeRentals", 0);
            model.addAttribute("overdueRentals", 0);
        }
        
        model.addAttribute("title", "도서관리 시스템");
        
        if (error != null) {
            switch (error) {
                case "login_required":
                    model.addAttribute("error", "로그인이 필요한 서비스입니다.");
                    break;
                case "login_failed":
                    model.addAttribute("error", "로그인에 실패했습니다.");
                    break;
            }
        }
        
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model, HttpSession session) {
        // 로그인 정보 추가
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        
        if (error != null) {
            switch (error) {
                case "login_required":
                    model.addAttribute("error", "로그인이 필요한 서비스입니다.");
                    break;
                case "login_failed":
                    model.addAttribute("error", "로그인에 실패했습니다.");
                    break;
            }
        }
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        // 로그인 정보 추가
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);
        
        return "register";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Map<String, Object> loginMember = (Map<String, Object>) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/";
        }
        
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("title", "대시보드 - 도서관리 시스템");
        return "dashboard";
    }
    
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        Map<String, Object> result = authService.login(loginData.get("loginId"), loginData.get("password"));
        
        if (Boolean.TRUE.equals(result.get("success"))) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("loginMember", result.get("data"));
        }

        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> memberData) {
        Map<String, Object> result = authService.register(memberData);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true, "message", "로그아웃되었습니다."));
    }
    
    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/books/search")
    @ResponseBody
    public ResponseEntity<Object> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";  // 200 OK만 반환, DB 조회 X
    }
}