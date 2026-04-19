package com.example.library.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<BookInfo> books;

    @Data
    public static class BookInfo {
        private Long id;
        private String title;
        private Integer publicationYear;
        private String authorName;
    }
}