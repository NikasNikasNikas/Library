package com.example.library.service;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public BookResponseDTO createBook(BookRequestDTO requestDTO) {
        Author author = authorRepository.findById(requestDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + requestDTO.getAuthorId()));

        Book book = new Book();
        book.setTitle(requestDTO.getTitle());
        book.setIsbn(requestDTO.getIsbn());
        book.setPublicationYear(requestDTO.getPublicationYear());
        book.setAuthor(author);

        Book saved = bookRepository.save(book);

        BookResponseDTO response = new BookResponseDTO();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setIsbn(saved.getIsbn());
        response.setPublicationYear(saved.getPublicationYear());
        response.setAuthorName(author.getName());

        return response;
    }
}