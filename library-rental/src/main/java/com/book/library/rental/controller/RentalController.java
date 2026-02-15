package com.book.library.rental.controller;

import com.book.library.common.dto.ApiResponse;
import com.book.library.common.exception.BusinessException;
import com.book.library.rental.dto.RentalCreateRequest;
import com.book.library.rental.dto.RentalResponse;
import com.book.library.rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    
    private final RentalService rentalService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<RentalResponse>>> getAllRentals() {
        List<RentalResponse> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(ApiResponse.success(rentals));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RentalResponse>> getRental(@PathVariable Long id) {
        RentalResponse rental = rentalService.getRental(id);
        return ResponseEntity.ok(ApiResponse.success(rental));
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<RentalResponse>>> getRentalsByMember(@PathVariable Long memberId) {
        List<RentalResponse> rentals = rentalService.getRentalsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(rentals));
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<RentalResponse>>> getRentalsByBook(@PathVariable Long bookId) {
        List<RentalResponse> rentals = rentalService.getRentalsByBook(bookId);
        return ResponseEntity.ok(ApiResponse.success(rentals));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<RentalResponse>> createRental(@RequestBody RentalCreateRequest request) {
        try {
            RentalResponse rental = rentalService.createRental(request);
            ApiResponse<RentalResponse> response = ApiResponse.success("도서가 성공적으로 대여되었습니다.", rental);
            return ResponseEntity
                    .created(URI.create("/api/rentals/" + rental.getId()))
                    .body(response);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
    
    @PatchMapping("/{id}/return")
    public ResponseEntity<ApiResponse<RentalResponse>> returnBook(@PathVariable Long id) {
        RentalResponse rental = rentalService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success("도서가 성공적으로 반납되었습니다.", rental));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.ok(ApiResponse.success("대여 기록이 성공적으로 삭제되었습니다.", null));
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
