package com.example.library.service;

import com.example.library.dao.BookJpaDao;
import com.example.library.dao.BookMyBatisDao;
import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookJpaDao jpaBookDao;

    private final BookMyBatisDao myBatisBookDao;

    private final AuthorRepository authorRepository;
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

        Book saved = jpaBookDao.save(book);

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

        myBatisBookDao.save(book);

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
        Book book = jpaBookDao.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (book.getCategories() == null) {
            book.setCategories(new java.util.HashSet<>());
        }

        book.getCategories().add(category);
        jpaBookDao.save(book);
    }
}