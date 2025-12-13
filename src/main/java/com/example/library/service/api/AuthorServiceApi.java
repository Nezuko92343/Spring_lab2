package com.example.library.service.api;

import com.example.library.model.Author;
import com.example.library.model.AuthorWithBooksRequest;
import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorServiceApi {

    Author createAuthor(Author author);
    Optional<Author> getAuthorById(Long id);
    List<Author> getAllAuthors();
    Author updateAuthor(Long id, Author author);
    void deleteAuthor(Long id);
    List<Author> searchByName(String name);
    List<Author> getAuthorsByCountry(String country);
    Author createAuthorWithBooks(AuthorWithBooksRequest request);
    List<Book> transferBooks(Long fromAuthorId, Long toAuthorId);
}
