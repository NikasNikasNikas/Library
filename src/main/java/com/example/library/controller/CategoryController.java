package com.example.library.controller;

import com.example.library.dto.CategoryRequestDTO;
import com.example.library.dto.CategoryResponseDTO;
import com.example.library.entity.Category;
import com.example.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(category -> {
            CategoryResponseDTO dto = new CategoryResponseDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setDescription(category.getDescription());

            if (category.getBooks() != null && !category.getBooks().isEmpty()) {
                List<CategoryResponseDTO.BookInfo> bookDTOs = category.getBooks().stream()
                        .map(book -> {
                            CategoryResponseDTO.BookInfo bookInfo = new CategoryResponseDTO.BookInfo();
                            bookInfo.setId(book.getId());
                            bookInfo.setTitle(book.getTitle());
                            bookInfo.setPublicationYear(book.getPublicationYear());
                            if (book.getAuthor() != null) {
                                bookInfo.setAuthorName(book.getAuthor().getName());
                            }
                            return bookInfo;
                        })
                        .collect(Collectors.toList());
                dto.setBooks(bookDTOs);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return categoryRepository.save(category);
    }
}