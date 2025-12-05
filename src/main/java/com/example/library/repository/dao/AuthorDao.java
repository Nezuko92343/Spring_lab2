package com.example.library.repository.dao;

import com.example.library.model.Author;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Author entity.
 * Provides CRUD operations and search functionality.
 */
public interface AuthorDao {

    /**
     * Creates a new author in the database.
     *
     * @param author the author to create
     * @return the generated ID of the created author
     */
    Long create(Author author);

    /**
     * Retrieves an author by their ID.
     *
     * @param id the author ID
     * @return an Optional containing the author if found, empty otherwise
     */
    Optional<Author> findById(Long id);

    /**
     * Retrieves all authors from the database.
     *
     * @return a list of all authors
     */
    List<Author> findAll();

    /**
     * Updates an existing author.
     *
     * @param author the author with updated values
     * @return true if the update was successful, false otherwise
     */
    boolean update(Author author);

    /**
     * Deletes an author by their ID.
     *
     * @param id the author ID
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(Long id);

    /**
     * Searches authors by name (partial match, case-insensitive).
     *
     * @param name the name to search for
     * @return a list of matching authors
     */
    List<Author> findByNameContaining(String name);

    /**
     * Searches authors by country.
     *
     * @param country the country to filter by
     * @return a list of authors from the specified country
     */
    List<Author> findByCountry(String country);

    /**
     * Finds an author by exact name.
     *
     * @param name the exact name to find
     * @return an Optional containing the author if found, empty otherwise
     */
    Optional<Author> findByName(String name);
}
