package com.example.library.controller.rest;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.dto.BookWithAuthorRequest;
import com.example.library.service.BookServiceJpa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "API для управління книгами")
public class BookRestController {

    private final BookServiceJpa bookService;

    public BookRestController(BookServiceJpa bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(
        summary = "Отримати всі книги",
        description = "Повертає список усіх книг у бібліотеці"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список книг успішно отримано"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Отримати книгу за ID",
        description = "Повертає детальну інформацію про книгу за її унікальним ідентифікатором"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Книгу знайдено"),
        @ApiResponse(responseCode = "404", description = "Книгу не знайдено", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<BookDTO> getBookById(
        @Parameter(description = "ID книги", required = true)
        @PathVariable Long id
    ) {
        BookDTO book = bookService.getBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
        summary = "Створити нову книгу",
        description = "Додає нову книгу до бібліотеки. Автор повинен існувати в базі даних"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Книгу успішно створено"),
        @ApiResponse(responseCode = "400", description = "Некоректні дані запиту", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<BookDTO> createBook(
        @Parameter(description = "Дані нової книги", required = true)
        @RequestBody BookDTO bookDTO
    ) {
        try {
            BookDTO created = bookService.createBook(bookDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Оновити книгу",
        description = "Оновлює інформацію про існуючу книгу"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Книгу успішно оновлено"),
        @ApiResponse(responseCode = "404", description = "Книгу не знайдено", content = @Content),
        @ApiResponse(responseCode = "400", description = "Некоректні дані запиту", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<BookDTO> updateBook(
        @Parameter(description = "ID книги", required = true)
        @PathVariable Long id,
        @Parameter(description = "Оновлені дані книги", required = true)
        @RequestBody BookDTO bookDTO
    ) {
        try {
            BookDTO updated = bookService.updateBook(id, bookDTO);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Видалити книгу",
        description = "Видаляє книгу з бібліотеки за її ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Книгу успішно видалено", content = @Content),
        @ApiResponse(responseCode = "404", description = "Книгу не знайдено", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<Void> deleteBook(
        @Parameter(description = "ID книги", required = true)
        @PathVariable Long id
    ) {
        boolean deleted = bookService.deleteBook(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search/rating")
    @Operation(
        summary = "Знайти книги за рейтингом",
        description = "Повертає список книг з вказаним рейтингом (0+, 6+, 12+, 16+, 18+)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<BookDTO>> findByRating(
        @Parameter(description = "Рейтинг книги (наприклад, 0+, 18+)", required = true)
        @RequestParam String rating
    ) {
        return ResponseEntity.ok(bookService.findByRating(rating));
    }

    @GetMapping("/search/year")
    @Operation(
        summary = "Знайти книги за роком видання",
        description = "Повертає список книг, виданих у вказаному діапазоні років (використовує @NamedQuery)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "400", description = "Некоректні параметри запиту", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<BookDTO>> findByPublicationYear(
        @Parameter(description = "Початковий рік", required = true)
        @RequestParam Integer startYear,
        @Parameter(description = "Кінцевий рік", required = true)
        @RequestParam Integer endYear
    ) {
        return ResponseEntity.ok(bookService.findByPublicationYearBetween(startYear, endYear));
    }

    @GetMapping("/search/author")
    @Operation(
        summary = "Знайти книги за ім'ям автора",
        description = "Повертає список книг написаних автором з вказаним ім'ям (Spring Data JPA метод)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<BookDTO>> findByAuthorName(
        @Parameter(description = "Ім'я автора", required = true)
        @RequestParam String name
    ) {
        return ResponseEntity.ok(bookService.findByAuthorName(name));
    }

    @GetMapping("/search/title")
    @Operation(
        summary = "Пошук книг за назвою",
        description = "Повертає список книг, назва яких містить вказаний текст"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<BookDTO>> searchByTitle(
        @Parameter(description = "Частина назви книги", required = true)
        @RequestParam String title
    ) {
        return ResponseEntity.ok(bookService.searchByTitle(title));
    }

    @GetMapping("/search/keyword")
    @Operation(
        summary = "Пошук книг за ключовим словом",
        description = "Повертає список книг, ключові слова яких містять вказаний текст"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пошук виконано успішно"),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера", content = @Content)
    })
    public ResponseEntity<List<BookDTO>> searchByKeyword(
        @Parameter(description = "Ключове слово", required = true)
        @RequestParam String keyword
    ) {
        return ResponseEntity.ok(bookService.searchByKeyword(keyword));
    }

    @PostMapping("/with-author")
    @Operation(
        summary = "Створити книгу з новим автором",
        description = "Транзакційний метод: створює нового автора та книгу атомарно. Якщо створення книги не вдається, автор також не буде збережений"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Книгу та автора успішно створено"),
        @ApiResponse(responseCode = "400", description = "Некоректні дані запиту", content = @Content),
        @ApiResponse(responseCode = "500", description = "Внутрішня помилка сервера (транзакція відкочена)", content = @Content)
    })
    public ResponseEntity<BookDTO> addBookWithNewAuthor(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Запит з даними книги та автора",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BookWithAuthorRequest.class)
            )
        )
        @RequestBody BookWithAuthorRequest request
    ) {
        try {
            BookDTO created = bookService.addBookWithNewAuthor(
                request.getBookDTO(),
                request.getAuthorDTO()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
