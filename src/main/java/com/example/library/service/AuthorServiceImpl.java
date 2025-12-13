package com.example.library.service;

import com.example.library.model.Author;
import com.example.library.model.AuthorWithBooksRequest;
import com.example.library.model.Book;
import com.example.library.repository.dao.AuthorDao;
import com.example.library.repository.dao.BookDao;
import com.example.library.service.api.AuthorServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorServiceApi {

    private final AuthorDao authorDao;
    private final BookDao bookDao;

    @Override
    @Transactional
    public Author createAuthor(Author author) {
        Long id = authorDao.create(author);
        return authorDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created author"));
    }

    @Override
    public Optional<Author> getAuthorById(Long id) {
        return authorDao.findById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.findAll();
    }

    @Override
    @Transactional
    public Author updateAuthor(Long id, Author author) {
        if (authorDao.findById(id).isEmpty()) {
            throw new NoSuchElementException("Author not found with id: " + id);
        }
        author.setId(id);
        authorDao.update(author);
        return authorDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve updated author"));
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (authorDao.findById(id).isEmpty()) {
            throw new NoSuchElementException("Author not found with id: " + id);
        }
        authorDao.delete(id);
    }

    @Override
    public List<Author> searchByName(String name) {
        return authorDao.findByNameContaining(name);
    }

    @Override
    public List<Author> getAuthorsByCountry(String country) {
        return authorDao.findByCountry(country);
    }

    @Override
    @Transactional
    public Author createAuthorWithBooks(AuthorWithBooksRequest request) {
        log.info("Starting transactional creation of author with books");

        Author author = request.getAuthor();
        Long authorId = authorDao.create(author);
        log.info("Created author with id: {}", authorId);

        if (request.getBooks() != null && !request.getBooks().isEmpty()) {
            for (AuthorWithBooksRequest.BookCreateRequest bookRequest : request.getBooks()) {
                Book book = Book.builder()
                        .title(bookRequest.getTitle())
                        .authorId(authorId)
                        .keywords(bookRequest.getKeywords())
                        .imagePath(bookRequest.getImagePath())
                        .rating(bookRequest.getRating())
                        .publicationYear(bookRequest.getPublicationYear())
                        .isbn(bookRequest.getIsbn())
                        .build();

                Long bookId = bookDao.create(book);
                log.info("Created book '{}' with id: {}", book.getTitle(), bookId);
            }
        }

        return authorDao.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created author"));
    }

    @Override
    @Transactional
    public List<Book> transferBooks(Long fromAuthorId, Long toAuthorId) {
        log.info("Starting transactional transfer of books from author {} to author {}",
                fromAuthorId, toAuthorId);

        authorDao.findById(fromAuthorId)
                .orElseThrow(() -> new NoSuchElementException("Source author not found with id: " + fromAuthorId));
        authorDao.findById(toAuthorId)
                .orElseThrow(() -> new NoSuchElementException("Target author not found with id: " + toAuthorId));

        List<Book> booksToTransfer = bookDao.findByAuthorId(fromAuthorId);
        List<Book> transferredBooks = new ArrayList<>();

        for (Book book : booksToTransfer) {
            book.setAuthorId(toAuthorId);
            bookDao.update(book);
            log.info("Transferred book '{}' (id: {}) to author {}", book.getTitle(), book.getId(), toAuthorId);
            transferredBooks.add(bookDao.findById(book.getId()).orElse(book));
        }

        log.info("Successfully transferred {} books", transferredBooks.size());
        return transferredBooks;
    }
}
