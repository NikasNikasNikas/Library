package com.example.library.controller;

import com.example.library.dto.AuthorResponseDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
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

        return authors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuthorResponseDTO convertToDTO(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBirthDate(author.getBirthDate());
        dto.setCountry(author.getCountry());

        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            List<BookResponseDTO> bookDTOs = author.getBooks().stream()
                    .map(this::convertBookToDTO)
                    .collect(Collectors.toList());
            dto.setBooks(bookDTOs);
        }

        return dto;
    }

    private BookResponseDTO convertBookToDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());

        // Set author name
        if (book.getAuthor() != null) {
            dto.setAuthorName(book.getAuthor().getName());
        }

        // Set category names
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            Set<String> categoryNames = book.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toSet());
            dto.setCategoryNames(categoryNames);
        }

        return dto;
    }
}