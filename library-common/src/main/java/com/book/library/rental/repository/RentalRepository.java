package com.book.library.rental.repository;

import com.book.library.Enums;
import com.book.library.rental.domain.Rental;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class RentalRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public RentalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<Rental> rowMapper = (rs, rowNum) -> {
        Rental rental = new Rental();
        rental.setId(rs.getLong("rental_id"));
        rental.setMemberId(rs.getLong("member_id"));
        rental.setBookId(rs.getLong("book_id"));
        rental.setRentalStatus(Enums.RentalStatus.values()[rs.getInt("rental_status")]);
        rental.setRentalDate(rs.getTimestamp("rental_date").toInstant());
        rental.setDueDate(rs.getTimestamp("due_date").toInstant());
        if (rs.getTimestamp("return_date") != null) {
            rental.setReturnDate(rs.getTimestamp("return_date").toInstant());
        }
        rental.setRemarks(rs.getString("remarks"));
        return rental;
    };
    
    public List<Rental> findAll() {
        return jdbcTemplate.query("SELECT * FROM rental", rowMapper);
    }
    
    public Optional<Rental> findById(Long id) {
        List<Rental> results = jdbcTemplate.query(
            "SELECT * FROM rental WHERE rental_id = ?", rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public List<Rental> findByMemberId(Long memberId) {
        return jdbcTemplate.query(
            "SELECT * FROM rental WHERE member_id = ?", rowMapper, memberId);
    }
    
    public List<Rental> findByBookId(Long bookId) {
        return jdbcTemplate.query(
            "SELECT * FROM rental WHERE book_id = ?", rowMapper, bookId);
    }
    
    public Rental save(Rental rental) {
        if (rental.getId() == null) {
            return insert(rental);
        } else {
            update(rental);
            return rental;
        }
    }
    
    private Rental insert(Rental rental) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO rental (member_id, book_id, rental_status, rental_date, due_date, return_date, remarks) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, rental.getMemberId());
            ps.setLong(2, rental.getBookId());
            ps.setInt(3, rental.getRentalStatus().ordinal());
            ps.setTimestamp(4, java.sql.Timestamp.from(rental.getRentalDate()));
            ps.setTimestamp(5, java.sql.Timestamp.from(rental.getDueDate()));
            ps.setTimestamp(6, rental.getReturnDate() != null ? 
                java.sql.Timestamp.from(rental.getReturnDate()) : null);
            ps.setString(7, rental.getRemarks());
            return ps;
        }, keyHolder);
        
        rental.setId(keyHolder.getKey().longValue());
        return rental;
    }
    
    private void update(Rental rental) {
        jdbcTemplate.update(
            "UPDATE rental SET member_id=?, book_id=?, rental_status=?, rental_date=?, " +
            "due_date=?, return_date=?, remarks=? WHERE rental_id=?",
            rental.getMemberId(), rental.getBookId(), rental.getRentalStatus().ordinal(),
            java.sql.Timestamp.from(rental.getRentalDate()),
            java.sql.Timestamp.from(rental.getDueDate()),
            rental.getReturnDate() != null ? java.sql.Timestamp.from(rental.getReturnDate()) : null,
            rental.getRemarks(), rental.getId());
    }
    
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM rental WHERE rental_id = ?", Integer.class, id);
        return count != null && count > 0;
    }
    
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM rental WHERE rental_id = ?", id);
    }
}
