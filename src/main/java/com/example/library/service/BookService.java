package com.example.library.service;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.mybatis.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository jpaBookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper myBatisBookMapper;

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
        response.setAuthorName(author.getName());  // Set author name

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
        response.setAuthorName(author.getName());  // Set author name

        return response;
    }
}