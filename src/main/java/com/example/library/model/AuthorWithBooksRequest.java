package com.example.library.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request object for creating an author with their books in a single transaction")
public class AuthorWithBooksRequest {

    @Valid
    @NotNull(message = "Author is required")
    @Schema(description = "The author to create", requiredMode = Schema.RequiredMode.REQUIRED)
    private Author author;

    @Valid
    @Schema(description = "List of books to create for the author")
    private List<BookCreateRequest> books;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Request object for creating a book (without author ID, as it will be set from the created author)")
    public static class BookCreateRequest {

        @NotNull(message = "Title is required")
        @Schema(description = "Title of the book", example = "The Shining", requiredMode = Schema.RequiredMode.REQUIRED)
        private String title;

        @Schema(description = "Keywords/tags for the book", example = "horror, psychological")
        private String keywords;

        @Schema(description = "Path to the book cover image", example = "/img/shining.jpg")
        private String imagePath;

        @Schema(description = "Age rating of the book", example = "18+")
        private String rating;

        @Schema(description = "Year of publication", example = "1977")
        private Integer publicationYear;

        @Schema(description = "ISBN of the book", example = "978-0-385-12167-5")
        private String isbn;
    }
}
