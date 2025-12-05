package com.example.library.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Book entity representing a book in the library")
public class Book {

    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Schema(description = "Title of the book", example = "11/22/63", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotNull(message = "Author ID is required")
    @Schema(description = "ID of the book's author", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long authorId;

    @Schema(description = "Author name (populated from join)", example = "Stephen King")
    private String authorName;

    @Size(max = 500, message = "Keywords must not exceed 500 characters")
    @Schema(description = "Keywords/tags for the book", example = "thriller, time travel, history")
    private String keywords;

    @Size(max = 255, message = "Image path must not exceed 255 characters")
    @Schema(description = "Path to the book cover image", example = "/img/112263.jpg")
    private String imagePath;

    @Schema(description = "Age rating of the book", example = "16+", allowableValues = {"0+", "6+", "12+", "16+", "18+"})
    private String rating;

    @Schema(description = "Year of publication", example = "2011")
    private Integer publicationYear;

    @Size(max = 20, message = "ISBN must not exceed 20 characters")
    @Schema(description = "International Standard Book Number", example = "978-1-4516-2728-2")
    private String isbn;
}
