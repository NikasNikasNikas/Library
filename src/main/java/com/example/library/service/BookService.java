package com.example.library.service;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.repository.mybatis.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository jpaBookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper myBatisBookMapper;
    private final CategoryRepository categoryRepository;


    @Transactional
    public BookResponseDTO createBookWithJPA(BookRequestDTO dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAuthor(author);

        Book saved = jpaBookRepository.save(book);

        BookResponseDTO response = new BookResponseDTO();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setIsbn(saved.getIsbn());
        response.setPublicationYear(saved.getPublicationYear());
        response.setAuthorName(author.getName());

        return response;
    }

    @Transactional
    public BookResponseDTO createBookWithMyBatis(BookRequestDTO dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAuthor(author);

        myBatisBookMapper.insert(book);

        BookResponseDTO response = new BookResponseDTO();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setIsbn(book.getIsbn());
        response.setPublicationYear(book.getPublicationYear());
        response.setAuthorName(author.getName());

        return response;
    }

    @Transactional
    public void addCategoryToBook(Long bookId, Long categoryId) {
        Book book = jpaBookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        // Initialize collections if null
        if (book.getCategories() == null) {
            book.setCategories(new java.util.HashSet<>());
        }
        if (category.getBooks() == null) {
            category.setBooks(new java.util.HashSet<>());
        }

        // Add to both sides of the relationship
        book.getCategories().add(category);
        category.getBooks().add(book);

        // Save only the book (cascade will handle the relationship)
        jpaBookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public Set<Category> getCategoriesForBook(Long bookId) {
        Book book = jpaBookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return book.getCategories();
    }

    @Transactional
    public void removeCategoryFromBook(Long bookId, Long categoryId) {
        Book book = jpaBookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        book.getCategories().remove(category);
        category.getBooks().remove(book);

        jpaBookRepository.save(book);
    }

}