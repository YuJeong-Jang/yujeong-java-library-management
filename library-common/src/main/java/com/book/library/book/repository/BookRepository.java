package com.book.library.book.repository;

import com.book.library.Enums;
import com.book.library.book.domain.Book;
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
public class BookRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private final RowMapper<Book> rowMapper = (rs, rowNum) -> {
        Book book = new Book();
        book.setId(rs.getLong("book_id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        if (rs.getDate("publish_date") != null) {
            book.setPublishDate(rs.getDate("publish_date").toLocalDate());
        }
        book.setCategory(rs.getString("category"));
        book.setTotalQuantity(rs.getInt("total_quantity"));
        book.setAvailableQty(rs.getInt("available_qty"));
        book.setStatus(Enums.BookStatus.values()[rs.getInt("status")]);
        book.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        book.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return book;
    };
    
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", rowMapper);
    }
    
    public Optional<Book> findById(Long id) {
        List<Book> results = jdbcTemplate.query(
            "SELECT * FROM book WHERE book_id = ?", rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public List<Book> findByTitleContainingOrAuthorContaining(String title, String author) {
        return jdbcTemplate.query(
            "SELECT * FROM book WHERE title LIKE ? OR author LIKE ?",
            rowMapper, "%" + title + "%", "%" + author + "%");
    }
    
    public Book save(Book book) {
        if (book.getId() == null) {
            return insert(book);
        } else {
            update(book);
            return book;
        }
    }
    
    private Book insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Instant now = Instant.now();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO book (isbn, title, author, publisher, publish_date, category, " +
                "total_quantity, available_qty, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getPublisher());
            ps.setObject(5, book.getPublishDate());
            ps.setString(6, book.getCategory());
            ps.setInt(7, book.getTotalQuantity());
            ps.setInt(8, book.getAvailableQty());
            ps.setInt(9, book.getStatus().ordinal());
            ps.setTimestamp(10, java.sql.Timestamp.from(now));
            ps.setTimestamp(11, java.sql.Timestamp.from(now));
            return ps;
        }, keyHolder);
        
        book.setId(keyHolder.getKey().longValue());
        book.setCreatedAt(now);
        book.setUpdatedAt(now);
        return book;
    }
    
    private void update(Book book) {
        Instant now = Instant.now();
        jdbcTemplate.update(
            "UPDATE book SET isbn=?, title=?, author=?, publisher=?, publish_date=?, " +
            "category=?, total_quantity=?, available_qty=?, status=?, updated_at=? " +
            "WHERE book_id=?",
            book.getIsbn(), book.getTitle(), book.getAuthor(), book.getPublisher(),
            book.getPublishDate(), book.getCategory(), book.getTotalQuantity(),
            book.getAvailableQty(), book.getStatus().ordinal(),
            java.sql.Timestamp.from(now), book.getId());
        book.setUpdatedAt(now);
    }
    
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM book WHERE book_id = ?", Integer.class, id);
        return count != null && count > 0;
    }
    
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id = ?", id);
    }
}
