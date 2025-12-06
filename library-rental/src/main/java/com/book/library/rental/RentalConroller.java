package com.book.library.rental;

import com.book.library.rental.domain.Rental;
import com.book.library.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library/rental")
@RequiredArgsConstructor
public class RentalConroller {
    private final RentalRepository rentalRepository;

    @GetMapping
    public List<Rental> getRentals() { return rentalRepository.findAll(); }

    @PostMapping
    public Rental saveRental(@RequestBody Rental rental) { return rentalRepository.save(rental); }

}
