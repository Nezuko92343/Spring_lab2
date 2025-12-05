package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.dao.BookDao;
import com.example.library.service.api.BookServiceApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementation of BookServiceApi.
 * Provides business logic for book operations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookServiceApi {

    private final BookDao bookDao;

    @Override
    @Transactional
    public Book createBook(Book book) {
        Long id = bookDao.create(book);
        return bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created book"));
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Override
    @Transactional
    public Book updateBook(Long id, Book book) {
        if (bookDao.findById(id).isEmpty()) {
            throw new NoSuchElementException("Book not found with id: " + id);
        }
        book.setId(id);
        bookDao.update(book);
        return bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated book"));
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (bookDao.findById(id).isEmpty()) {
            throw new NoSuchElementException("Book not found with id: " + id);
        }
        bookDao.delete(id);
    }

    @Override
    public List<Book> searchByTitle(String title) {
        return bookDao.findByTitleContaining(title);
    }

    @Override
    public List<Book> getBooksByAuthor(Long authorId) {
        return bookDao.findByAuthorId(authorId);
    }

    @Override
    public List<Book> getBooksByRating(String rating) {
        return bookDao.findByRating(rating);
    }

    @Override
    public List<Book> searchByKeyword(String keyword) {
        return bookDao.findByKeyword(keyword);
    }

    @Override
    public List<Book> search(String title, Long authorId, String keyword) {
        return bookDao.search(title, authorId, keyword);
    }
}
