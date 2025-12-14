package com.example.library.controller.rest;

import com.example.library.dto.AuthorDTO;
import com.example.library.service.AuthorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "API для управління авторами")
public class AuthorRestController {

    private final AuthorServiceImpl authorService;

    public AuthorRestController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(
        summary = "Отримати всіх авторів",
        description = "Повертає список усіх авторів у базі даних"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список авторів успішно отримано"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Отримати автора за ID",
        description = "Повертає детальну інформацію про автора за його унікальним ідентифікатором"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Автора знайдено"),
        @ApiResponse(responseCode = "404", description = "Автора не знайдено", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<AuthorDTO> getAuthorById(
        @Parameter(description = "ID автора", required = true)
        @PathVariable Long id
    ) {
        AuthorDTO author = authorService.getAuthorById(id);
        return author != null ? ResponseEntity.ok(author) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
        summary = "Створити нового автора",
        description = "Додає нового автора до бази даних"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Автора успішно створено"),
        @ApiResponse(responseCode = "400", description = "Некоректні дані запиту", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<AuthorDTO> createAuthor(
        @Parameter(description = "Дані нового автора", required = true)
        @RequestBody AuthorDTO authorDTO
    ) {
        AuthorDTO created = authorService.createAuthor(authorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Оновити автора",
        description = "Оновлює інформацію про існуючого автора"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Автора успішно оновлено"),
        @ApiResponse(responseCode = "404", description = "Автора не знайдено", content = @Content),
        @ApiResponse(responseCode = "400", description = "Некоректні дані запиту", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<AuthorDTO> updateAuthor(
        @Parameter(description = "ID автора", required = true)
        @PathVariable Long id,
        @Parameter(description = "Оновлені дані автора", required = true)
        @RequestBody AuthorDTO authorDTO
    ) {
        AuthorDTO updated = authorService.updateAuthor(id, authorDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Видалити автора",
        description = "Видаляє автора з бази даних за його ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Автора успішно видалено", content = @Content),
        @ApiResponse(responseCode = "404", description = "Автора не знайдено", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<Void> deleteAuthor(
        @Parameter(description = "ID автора", required = true)
        @PathVariable Long id
    ) {
        boolean deleted = authorService.deleteAuthor(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search/country")
    @Operation(
        summary = "Знайти авторів за країною",
        description = "Повертає список авторів з вказаної країни (використовує JPQL з @Query)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<AuthorDTO>> findByCountry(
        @Parameter(description = "Назва країни", required = true)
        @RequestParam String country
    ) {
        return ResponseEntity.ok(authorService.findByCountry(country));
    }

    @GetMapping("/search/name")
    @Operation(
        summary = "Пошук авторів за ім'ям",
        description = "Повертає список авторів, ім'я яких містить вказаний текст (Spring Data JPA метод)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<AuthorDTO>> findByNameContaining(
        @Parameter(description = "Частина імені автора", required = true)
        @RequestParam String name
    ) {
        return ResponseEntity.ok(authorService.findByNameContaining(name));
    }
}
