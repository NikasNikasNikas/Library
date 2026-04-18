package com.example.library.service;

import com.example.library.dto.AuthorRequestDTO;
import com.example.library.dto.AuthorResponseDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAllWithBooks().stream()
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


    private AuthorResponseDTO convertToDTO(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setBirthDate(author.getBirthDate());
        dto.setCountry(author.getCountry());

        if (author.getBooks() != null) {
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
        if (book.getAuthor() != null) {
            dto.setAuthorName(book.getAuthor().getName());
        }
        return dto;
    }
}