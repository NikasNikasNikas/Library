package com.example.library.service;

import com.example.library.dto.CategoryRequestDTO;
import com.example.library.dto.CategoryResponseDTO;
import com.example.library.entity.Category;
import com.example.library.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        Category saved = categoryRepository.save(category);
        return convertToDTO(saved);
    }

    private CategoryResponseDTO convertToDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        // Add books if needed
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
    }
}