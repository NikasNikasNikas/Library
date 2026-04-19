package com.example.library.service;

import com.example.library.dto.AuthorRequestDTO;
import com.example.library.dto.AuthorResponseDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<AuthorResponseDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAllWithBooks();

        return authors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorRequestDTO requestDTO) {
        Author author = new Author();
        author.setName(requestDTO.getName());
        author.setBirthDate(requestDTO.getBirthDate());
        author.setCountry(requestDTO.getCountry());

        Author saved = authorRepository.save(author);
        return convertToDTO(saved);
    }

    @Transactional
    public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO requestDTO) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        author.setName(requestDTO.getName());
        author.setBirthDate(requestDTO.getBirthDate());
        author.setCountry(requestDTO.getCountry());

        Author updated = authorRepository.save(author);
        return convertToDTO(updated);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        authorRepository.delete(author);
    }

    @Transactional(readOnly = true)
    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    // DTO conversion methods moved from controller to service
    private AuthorResponseDTO convertToDTO(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBirthDate(author.getBirthDate());
        dto.setCountry(author.getCountry());

        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            // Use a Map to ensure unique books by ID
            Map<Long, BookResponseDTO> uniqueBooks = new LinkedHashMap<>();

            for (Book book : author.getBooks()) {
                if (!uniqueBooks.containsKey(book.getId())) {
                    uniqueBooks.put(book.getId(), convertBookToDTO(book));
                }
            }

            dto.setBooks(new ArrayList<>(uniqueBooks.values()));
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

        // Set category names (using Set to ensure unique categories)
        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            Set<String> categoryNames = book.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toSet());
            dto.setCategoryNames(categoryNames);
        }

        return dto;
    }
}