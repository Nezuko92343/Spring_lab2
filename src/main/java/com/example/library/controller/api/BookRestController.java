package com.example.library.controller.api;

import com.example.library.model.Book;
import com.example.library.service.api.BookServiceApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Book operations.
 * Provides CRUD and search endpoints for books.
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "API for managing books in the library")
public class BookRestController {

    private final BookServiceApi bookService;

    @Operation(
            summary = "Get all books",
            description = "Retrieves a list of all books in the library with their authors"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of books",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Operation(
            summary = "Get book by ID",
            description = "Retrieves a specific book by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the book",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found with the given ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "Unique identifier of the book", required = true, example = "1")
            @PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new book",
            description = "Creates a new book in the library. The book ID is auto-generated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Book> createBook(
            @Parameter(description = "Book data to create", required = true)
            @Valid @RequestBody Book book) {
        Book created = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Update a book",
            description = "Updates an existing book with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Book successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Book.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found with the given ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid book data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "Unique identifier of the book to update", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated book data", required = true)
            @Valid @RequestBody Book book) {
        Book updated = bookService.updateBook(id, book);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes a book from the library by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Book successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found with the given ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Unique identifier of the book to delete", required = true, example = "1")
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search books by title",
            description = "Searches for books with titles containing the given text (case-insensitive)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved matching books",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            )
    })
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(
            @Parameter(description = "Title text to search for", required = true, example = "King")
            @RequestParam String title) {
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @Operation(
            summary = "Get books by author",
            description = "Retrieves all books written by the specified author"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved books by author",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            )
    })
    @GetMapping("/search/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @Parameter(description = "Author ID to filter by", required = true, example = "1")
            @PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @Operation(
            summary = "Get books by rating",
            description = "Retrieves all books with the specified age rating"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved books with rating",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            )
    })
    @GetMapping("/search/rating")
    public ResponseEntity<List<Book>> getBooksByRating(
            @Parameter(description = "Age rating to filter by", required = true, example = "16+",
                    schema = @Schema(allowableValues = {"0+", "6+", "12+", "16+", "18+"}))
            @RequestParam String rating) {
        return ResponseEntity.ok(bookService.getBooksByRating(rating));
    }

    @Operation(
            summary = "Search books by keyword",
            description = "Searches for books with keywords containing the given text"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved matching books",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            )
    })
    @GetMapping("/search/keyword")
    public ResponseEntity<List<Book>> searchByKeyword(
            @Parameter(description = "Keyword to search for", required = true, example = "horror")
            @RequestParam String keyword) {
        return ResponseEntity.ok(bookService.searchByKeyword(keyword));
    }

    @Operation(
            summary = "Advanced search",
            description = "Searches books using multiple optional criteria. All provided criteria must match."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved matching books",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(
            @Parameter(description = "Title text to search for", example = "11/22/63")
            @RequestParam(required = false) String title,
            @Parameter(description = "Author ID to filter by", example = "1")
            @RequestParam(required = false) Long authorId,
            @Parameter(description = "Keyword to search for", example = "thriller")
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(bookService.search(title, authorId, keyword));
    }

    @Schema(description = "Error response object")
    public record ErrorResponse(
            @Schema(description = "Error message", example = "Book not found with id: 999")
            String message,
            @Schema(description = "HTTP status code", example = "404")
            int status
    ) {}
}
