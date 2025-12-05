package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private String title;
    private String author;
    private String keywords;
    private String imagePath;
    private String rating;
}
