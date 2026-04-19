package com.example.library.controller;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Category;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/jpa")
    public BookResponseDTO createBookWithJPA(@Valid  @RequestBody BookRequestDTO dto) {
        System.out.println("Creating book with JPA: " + dto.getTitle());
        return bookService.createBookWithJPA(dto);
    }

    @PostMapping("/mybatis")
    public BookResponseDTO createBookWithMyBatis(@Valid @RequestBody BookRequestDTO dto) {
        System.out.println("Creating book with MyBatis: " + dto.getTitle());
        return bookService.createBookWithMyBatis(dto);
    }

    @PostMapping("/{bookId}/categories/{categoryId}")
    public String addCategoryToBook(
            @PathVariable Long bookId,
            @PathVariable Long categoryId) {
        System.out.println("Adding category " + categoryId + " to book " + bookId);
        bookService.addCategoryToBook(bookId, categoryId);
        return "Category added successfully!";
    }
}