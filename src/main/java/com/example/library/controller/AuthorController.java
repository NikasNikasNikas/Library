package com.example.library.controller;

import com.example.library.dto.AuthorResponseDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping
    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAllWithBooks();

        return authors.stream().map(author -> {
            AuthorResponseDTO dto = new AuthorResponseDTO();
            dto.setId(author.getId());
            dto.setName(author.getName());
            dto.setCountry(author.getCountry());
            dto.setBirthDate(author.getBirthDate());

            // Convert books to DTOs
            if (author.getBooks() != null) {
                List<BookResponseDTO> bookDTOs = author.getBooks().stream().map(book -> {
                    BookResponseDTO bookDTO = new BookResponseDTO();
                    bookDTO.setId(book.getId());
                    bookDTO.setTitle(book.getTitle());
                    bookDTO.setIsbn(book.getIsbn());
                    bookDTO.setPublicationYear(book.getPublicationYear());
                    return bookDTO;
                }).collect(Collectors.toList());
                dto.setBooks(bookDTOs);
            }

            return dto;
        }).collect(Collectors.toList());
    }
}