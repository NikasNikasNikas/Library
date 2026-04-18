package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by author
    List<Book> findByAuthorId(Long authorId);

    // Find books by title containing
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find book with its categories
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findByIdWithCategories(@Param("id") Long id);

    // Find all books with authors and categories
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.author LEFT JOIN FETCH b.categories")
    List<Book> findAllWithDetails();

    // Check if ISBN exists
    boolean existsByIsbn(String isbn);
}