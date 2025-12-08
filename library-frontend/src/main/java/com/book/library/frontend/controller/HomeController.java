package com.book.library.frontend.controller;

import com.book.library.book.response.BookResponse;
import com.book.library.member.response.MemberResponse;
import com.book.library.rental.resource.RentalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class HomeController {

    @Value("${library.book.url}")
    private String bookUrl;

    @Value("${library.member.url}")
    private String memberUrl;

    @Value("${library.rental.url}")
    private String rentalUrl;

    private final WebClient.Builder webClientBuilder;

    public HomeController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/")
    public String home() {
        return "index";  // 대시보드
    }

    @GetMapping("/members")
    public String members(Model model) {
        WebClient client = webClientBuilder.baseUrl(memberUrl).build();
        List<MemberResponse> members = client.get()
                .uri("/library/members")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MemberResponse>>() {})
                .block();
        model.addAttribute("members", members);
        model.addAttribute("title", "회원목록");
        model.addAttribute("activePage", "members");
        return "members/members";
    }

    @GetMapping("/books")
    public String books(Model model) {
        WebClient client = webClientBuilder.baseUrl(bookUrl).build();
        // library-book의 API 엔드포인트에 맞게 수정
        List<BookResponse> books = client.get()
                .uri("/library/books")  // book 모듈 API 경로
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BookResponse>>() {})
                .block();
        model.addAttribute("books", books);
        return "books/books";
    }

    @GetMapping("/rental")
    public String rentals(Model model) {
        WebClient client = webClientBuilder.baseUrl(rentalUrl).build();
        // library-book의 API 엔드포인트에 맞게 수정
        List<RentalResponse> books = client.get()
                .uri("/library/books")  // book 모듈 API 경로
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<RentalResponse>>() {})
                .block();
        model.addAttribute("rentals", books);
        return "rentals/rentals";
    }
}
