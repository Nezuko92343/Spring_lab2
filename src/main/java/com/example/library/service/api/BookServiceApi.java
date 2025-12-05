package com.example.library.service.api;

import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Book operations.
 */
public interface BookServiceApi {

    /**
     * Creates a new book.
     *
     * @param book the book to create
     * @return the created book with generated ID
     */
    Book createBook(Book book);

    /**
     * Retrieves a book by ID.
     *
     * @param id the book ID
     * @return an Optional containing the book if found
     */
    Optional<Book> getBookById(Long id);

    /**
     * Retrieves all books.
     *
     * @return list of all books
     */
    List<Book> getAllBooks();

    /**
     * Updates an existing book.
     *
     * @param id   the book ID
     * @param book the updated book data
     * @return the updated book
     */
    Book updateBook(Long id, Book book);

    /**
     * Deletes a book by ID.
     *
     * @param id the book ID
     */
    void deleteBook(Long id);

    /**
     * Searches books by title.
     *
     * @param title the title to search for
     * @return list of matching books
     */
    List<Book> searchByTitle(String title);

    /**
     * Gets books by author ID.
     *
     * @param authorId the author ID
     * @return list of books by the author
     */
    List<Book> getBooksByAuthor(Long authorId);

    /**
     * Gets books by rating.
     *
     * @param rating the rating to filter by
     * @return list of books with the rating
     */
    List<Book> getBooksByRating(String rating);

    /**
     * Searches books by keyword.
     *
     * @param keyword the keyword to search for
     * @return list of matching books
     */
    List<Book> searchByKeyword(String keyword);

    /**
     * Advanced search with multiple criteria.
     *
     * @param title    optional title filter
     * @param authorId optional author ID filter
     * @param keyword  optional keyword filter
     * @return list of matching books
     */
    List<Book> search(String title, Long authorId, String keyword);
}
