package com.example.library.dto;

import lombok.Data;

@Data
public class BookRequestDTO {
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Long authorId;
}