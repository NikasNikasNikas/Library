package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDTO {
    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must be less than 50 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
}