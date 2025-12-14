package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private Long authorId;
    private String authorName;
    private String keywords;
    private String imagePath;
    private String rating;
    private String isbn;
    private Integer publicationYear;
}
