package com.book.library.rental.controller;

import com.book.library.rental.domain.Rental;
import com.book.library.rental.repository.RentalRepository;
import com.book.library.rental.resource.RentalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/library/rentals")
@RequiredArgsConstructor
public class RentalConroller {
    private final RentalRepository rentalRepository;

    // 1) 전체 목록 조회 (GET /library/rentals)
    @GetMapping
    public List<RentalResponse> getRentals() {
        return rentalRepository.findAll().stream()
                .map(RentalResponse::from)
                .toList();
    }

    // 2) 단건 조회 (GET /library/rentals/{id})
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRental(@PathVariable Long id) {
        return rentalRepository.findById(id)
                .map(RentalResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3) 생성 (POST /library/rentals)
    @PostMapping
    public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody Rental request) {
        Rental saved = rentalRepository.save(request);
        return ResponseEntity
                .created(URI.create("/library/rentals/" + saved.getId()))
                .body(RentalResponse.from(saved));
    }

    // 4) 수정 (PUT /library/rentals/{id})
    @PutMapping("/{id}")
    public ResponseEntity<RentalResponse> updateRental(
            @PathVariable Long id,
            @Valid @RequestBody Rental request
    ) {
        return rentalRepository.findById(id)
                .map(existing -> {
                    existing.setMember(request.getMember());
                    existing.setBook(request.getBook());
                    existing.setRentalStatus(request.getRentalStatus());
                    existing.setRentalDate(request.getRentalDate());
                    existing.setDueDate(request.getDueDate());
                    existing.setReturnDate(request.getReturnDate());
                    existing.setRemarks(request.getRemarks());
                    Rental saved = rentalRepository.save(existing);
                    return ResponseEntity.ok(RentalResponse.from(saved));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 5) 삭제 (DELETE /library/rentals/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        if (!rentalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rentalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
