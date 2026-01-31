package com.book.library.book.repository;

import com.book.library.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title% OR b.author LIKE %:author%")
    List<Book> findByTitleContainingOrAuthorContaining(@Param("title") String title, @Param("author") String author);
    
    @Query("SELECT b FROM Book b WHERE b.category = :category")
    List<Book> findByCategory(@Param("category") String category);
    
    @Query("SELECT b FROM Book b WHERE b.author = :author")
    List<Book> findByAuthor(@Param("author") String author);
}
