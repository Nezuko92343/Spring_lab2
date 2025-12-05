package com.example.library.service.api;

import com.example.library.model.Author;
import com.example.library.model.AuthorWithBooksRequest;
import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Author operations.
 */
public interface AuthorServiceApi {

    /**
     * Creates a new author.
     *
     * @param author the author to create
     * @return the created author with generated ID
     */
    Author createAuthor(Author author);

    /**
     * Retrieves an author by ID.
     *
     * @param id the author ID
     * @return an Optional containing the author if found
     */
    Optional<Author> getAuthorById(Long id);

    /**
     * Retrieves all authors.
     *
     * @return list of all authors
     */
    List<Author> getAllAuthors();

    /**
     * Updates an existing author.
     *
     * @param id     the author ID
     * @param author the updated author data
     * @return the updated author
     */
    Author updateAuthor(Long id, Author author);

    /**
     * Deletes an author by ID (also deletes all their books).
     *
     * @param id the author ID
     */
    void deleteAuthor(Long id);

    /**
     * Searches authors by name.
     *
     * @param name the name to search for
     * @return list of matching authors
     */
    List<Author> searchByName(String name);

    /**
     * Gets authors by country.
     *
     * @param country the country to filter by
     * @return list of authors from the country
     */
    List<Author> getAuthorsByCountry(String country);

    /**
     * Creates an author with their books in a single transaction.
     * If any book creation fails, the entire operation is rolled back.
     *
     * @param request the request containing author and books data
     * @return the created author
     */
    Author createAuthorWithBooks(AuthorWithBooksRequest request);

    /**
     * Transfers all books from one author to another in a single transaction.
     *
     * @param fromAuthorId the source author ID
     * @param toAuthorId   the target author ID
     * @return list of transferred books
     */
    List<Book> transferBooks(Long fromAuthorId, Long toAuthorId);
}
