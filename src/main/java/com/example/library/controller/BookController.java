package com.example.library.controller;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/jpa")
    public BookResponseDTO createBookWithJPA(@RequestBody BookRequestDTO dto) {
        return bookService.createBookWithJPA(dto);
    }

    @PostMapping("/mybatis")
    public BookResponseDTO createBookWithMyBatis(@RequestBody BookRequestDTO dto) {
        return bookService.createBookWithMyBatis(dto);
    }
}