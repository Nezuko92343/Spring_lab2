package com.example.library.repository.dao;

import com.example.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Long create(Book book);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    boolean update(Book book);
    boolean delete(Long id);

    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByRating(String rating);
    List<Book> findByKeyword(String keyword);
    List<Book> search(String title, Long authorId, String keyword);
}
