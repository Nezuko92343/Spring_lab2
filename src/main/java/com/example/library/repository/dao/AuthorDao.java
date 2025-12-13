package com.example.library.repository.dao;

import com.example.library.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Long create(Author author);
    Optional<Author> findById(Long id);
    List<Author> findAll();
    boolean update(Author author);
    boolean delete(Long id);

    List<Author> findByNameContaining(String name);
    List<Author> findByCountry(String country);
    Optional<Author> findByName(String name);
}
