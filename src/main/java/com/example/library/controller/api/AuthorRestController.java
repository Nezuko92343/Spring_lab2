package com.example.library.controller.api;

import com.example.library.model.Author;
import com.example.library.model.AuthorWithBooksRequest;
import com.example.library.model.Book;
import com.example.library.service.api.AuthorServiceApi;
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
 * REST Controller for Author operations.
 * Provides CRUD, search, and transactional endpoints for authors.
 */
@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "API for managing authors in the library")
public class AuthorRestController {

    private final AuthorServiceApi authorService;

    @Operation(
            summary = "Get all authors",
            description = "Retrieves a list of all authors in the library"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of authors",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Author.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @Operation(
            summary = "Get author by ID",
            description = "Retrieves a specific author by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the author",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found with the given ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @Parameter(description = "Unique identifier of the author", required = true, example = "1")
            @PathVariable Long id) {
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new author",
            description = "Creates a new author in the library. The author ID is auto-generated."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Author successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid author data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @Parameter(description = "Author data to create", required = true)
            @Valid @RequestBody Author author) {
        Author created = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Update an author",
            description = "Updates an existing author with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Author successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found with the given ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid author data provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @Parameter(description = "Unique identifier of the author to update", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated author data", required = true)
            @Valid @RequestBody Author author) {
        Author updated = authorService.updateAuthor(id, author);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete an author",
            description = "Deletes an author from the library. All books by this author will also be deleted (CASCADE)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Author successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found with the given ID",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "Unique identifier of the author to delete", required = true, example = "1")
            @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search authors by name",
            description = "Searches for authors with names containing the given text (case-insensitive)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved matching authors",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Author.class))
                    )
            )
    })
    @GetMapping("/search/name")
    public ResponseEntity<List<Author>> searchByName(
            @Parameter(description = "Name text to search for", required = true, example = "King")
            @RequestParam String name) {
        return ResponseEntity.ok(authorService.searchByName(name));
    }

    @Operation(
            summary = "Get authors by country",
            description = "Retrieves all authors from the specified country"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved authors from country",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Author.class))
                    )
            )
    })
    @GetMapping("/search/country")
    public ResponseEntity<List<Author>> getAuthorsByCountry(
            @Parameter(description = "Country to filter by", required = true, example = "USA")
            @RequestParam String country) {
        return ResponseEntity.ok(authorService.getAuthorsByCountry(country));
    }

    @Operation(
            summary = "Create author with books (Transactional)",
            description = """
                Creates an author along with their books in a single transaction.
                If any operation fails (e.g., duplicate ISBN), the entire transaction is rolled back.
                This demonstrates ACID properties of database transactions.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Author and books successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data provided or constraint violation (e.g., duplicate ISBN)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Transaction rolled back due to error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/with-books")
    public ResponseEntity<Author> createAuthorWithBooks(
            @Parameter(description = "Author and books data to create", required = true)
            @Valid @RequestBody AuthorWithBooksRequest request) {
        Author created = authorService.createAuthorWithBooks(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
            summary = "Transfer books between authors (Transactional)",
            description = """
                Transfers all books from one author to another in a single transaction.
                If any book update fails, the entire transaction is rolled back.
                This demonstrates transactional updates across multiple records.
                """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Books successfully transferred",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Book.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Source or target author not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Transaction rolled back due to error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/{fromAuthorId}/transfer-books/{toAuthorId}")
    public ResponseEntity<List<Book>> transferBooks(
            @Parameter(description = "ID of the author to transfer books from", required = true, example = "1")
            @PathVariable Long fromAuthorId,
            @Parameter(description = "ID of the author to transfer books to", required = true, example = "2")
            @PathVariable Long toAuthorId) {
        List<Book> transferredBooks = authorService.transferBooks(fromAuthorId, toAuthorId);
        return ResponseEntity.ok(transferredBooks);
    }

    @Schema(description = "Error response object")
    public record ErrorResponse(
            @Schema(description = "Error message", example = "Author not found with id: 999")
            String message,
            @Schema(description = "HTTP status code", example = "404")
            int status
    ) {}
}
