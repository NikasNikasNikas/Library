package com.example.library.dto;

import lombok.Data;
import java.util.Set;

@Data
public class BookWithCategoriesDTO {
    private Long id;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private Set<String> categoryNames;
}