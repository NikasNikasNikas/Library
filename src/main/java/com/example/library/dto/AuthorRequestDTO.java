package com.example.library.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AuthorRequestDTO {
    private String name;
    private String country;
    private LocalDate birthDate;
}