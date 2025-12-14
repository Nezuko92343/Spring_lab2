package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWithAuthorRequest {
    private BookDTO bookDTO;
    private AuthorDTO authorDTO;
}
