package com.example.library.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class AuthorResponseDTO {
    private Long id;
    private String name;
    private String country;
    private LocalDate birthDate;
    private List<BookResponseDTO> books;  // Changed to use BookWithCategoriesDTO
}