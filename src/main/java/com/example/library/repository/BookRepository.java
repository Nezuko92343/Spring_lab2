package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Legacy in-memory BookRepository for the web UI (Thymeleaf templates).
 * This is kept for backward compatibility with the existing admin UI.
 */
@Repository
public class BookRepository {

    private final List<Book> books = new ArrayList<>();
    private Long nextId = 1L;

    public BookRepository() {
        initializeData();
        System.out.println("BookRepository ініціалізовано");
    }

    private void initializeData() {
        books.add(Book.builder()
                .id(nextId++)
                .title("11/22/63")
                .authorName("Стівен Кінг")
                .keywords("фантастика, трилер")
                .imagePath("/img/king.jpg")
                .rating("0+")
                .build());
        books.add(Book.builder()
                .id(nextId++)
                .title("Скажи мені")
                .authorName("Унн Фрейзер")
                .keywords("роман, психологія")
                .imagePath("/img/tell_me.jpg")
                .rating("18+")
                .build());
        books.add(Book.builder()
                .id(nextId++)
                .title("Краще не читай")
                .authorName("Катерина Орловська")
                .keywords("трилер, горор, проза")
                .imagePath("/img/book8.jpg")
                .rating("0+")
                .build());
        books.add(Book.builder()
                .id(nextId++)
                .title("Аутсайдер")
                .authorName("Стівен Кінг")
                .keywords("трилер, горор, фантастика")
                .imagePath("/img/book6.jpg")
                .rating("16+")
                .build());
        books.add(Book.builder()
                .id(nextId++)
                .title("Те, що бенкетує вночі")
                .authorName("Т. Кінгфішер")
                .keywords("горор, проза, детектив")
                .imagePath("/img/book4.jpg")
                .rating("12+")
                .build());
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public void addBook(Book book) {
        if (book.getId() == null) {
            book.setId(nextId++);
        }
        books.add(book);
    }

    public List<Book> search(String author, String title, String keyword) {
        return books.stream()
                .filter(b ->
                        (author == null || author.isEmpty() ||
                         (b.getAuthorName() != null && b.getAuthorName().toLowerCase().contains(author.toLowerCase()))) &&
                        (title == null || title.isEmpty() ||
                         (b.getTitle() != null && b.getTitle().toLowerCase().contains(title.toLowerCase()))) &&
                        (keyword == null || keyword.isEmpty() ||
                         (b.getKeywords() != null && b.getKeywords().toLowerCase().contains(keyword.toLowerCase())))
                )
                .collect(Collectors.toList());
    }

    public boolean deleteBook(String title) {
        return books.removeIf(book -> book.getTitle().equals(title));
    }

    public Book findByTitleAndAuthor(String title, String author) {
        return books.stream()
                .filter(b -> b.getTitle().equals(title) &&
                        (b.getAuthorName() != null && b.getAuthorName().equals(author)))
                .findFirst()
                .orElse(null);
    }
}
