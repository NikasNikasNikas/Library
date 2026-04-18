package com.example.library.controller;

import com.example.library.dto.CategoryRequestDTO;
import com.example.library.dto.CategoryResponseDTO;
import com.example.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CategoryRequestDTO dto) {
        return categoryService.createCategory(dto);
    }
}