package com.example.library.repository.dao;

import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Book entity.
 * Provides CRUD operations and search functionality.
 */
public interface BookDao {

    /**
     * Creates a new book in the database.
     *
     * @param book the book to create
     * @return the generated ID of the created book
     */
    Long create(Book book);

    /**
     * Retrieves a book by its ID.
     *
     * @param id the book ID
     * @return an Optional containing the book if found, empty otherwise
     */
    Optional<Book> findById(Long id);

    /**
     * Retrieves all books from the database.
     *
     * @return a list of all books
     */
    List<Book> findAll();

    /**
     * Updates an existing book.
     *
     * @param book the book with updated values
     * @return true if the update was successful, false otherwise
     */
    boolean update(Book book);

    /**
     * Deletes a book by its ID.
     *
     * @param id the book ID
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(Long id);

    /**
     * Searches books by title (partial match, case-insensitive).
     *
     * @param title the title to search for
     * @return a list of matching books
     */
    List<Book> findByTitleContaining(String title);

    /**
     * Searches books by author ID.
     *
     * @param authorId the author ID
     * @return a list of books by the specified author
     */
    List<Book> findByAuthorId(Long authorId);

    /**
     * Searches books by rating.
     *
     * @param rating the rating to filter by
     * @return a list of books with the specified rating
     */
    List<Book> findByRating(String rating);

    /**
     * Searches books by keyword (partial match in keywords field).
     *
     * @param keyword the keyword to search for
     * @return a list of matching books
     */
    List<Book> findByKeyword(String keyword);

    /**
     * Searches books by multiple criteria.
     *
     * @param title   optional title filter
     * @param authorId optional author ID filter
     * @param keyword optional keyword filter
     * @return a list of matching books
     */
    List<Book> search(String title, Long authorId, String keyword);
}
