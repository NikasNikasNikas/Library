package com.example.library.controller;

import com.example.library.dto.AuthorResponseDTO;
import com.example.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }
}