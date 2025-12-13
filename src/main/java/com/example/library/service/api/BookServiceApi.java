package com.example.library.service.api;

import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookServiceApi {
    Book createBook(Book book);
    Optional<Book> getBookById(Long id);
    List<Book> getAllBooks();
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    List<Book> searchByTitle(String title);
    List<Book> getBooksByAuthor(Long authorId);
    List<Book> getBooksByRating(String rating);
    List<Book> searchByKeyword(String keyword);
    List<Book> search(String title, Long authorId, String keyword);
}
