package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.repository.AuthorJpaRepository;
import com.example.library.repository.BookJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceJpa {

    private final BookJpaRepository bookRepository;
    private final AuthorJpaRepository authorRepository;

    public BookServiceJpa(BookJpaRepository bookRepository, AuthorJpaRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookDTO> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(author);
        book.setKeywords(bookDTO.getKeywords());
        book.setImagePath(bookDTO.getImagePath());
        book.setRating(bookDTO.getRating());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());

        Book saved = bookRepository.save(book);
        return convertToDTO(saved);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    if (bookDTO.getAuthorId() != null) {
                        Author author = authorRepository.findById(bookDTO.getAuthorId())
                                .orElseThrow(() -> new RuntimeException("Author not found"));
                        book.setAuthor(author);
                    }
                    book.setTitle(bookDTO.getTitle());
                    book.setKeywords(bookDTO.getKeywords());
                    book.setImagePath(bookDTO.getImagePath());
                    book.setRating(bookDTO.getRating());
                    book.setIsbn(bookDTO.getIsbn());
                    book.setPublicationYear(bookDTO.getPublicationYear());
                    return convertToDTO(bookRepository.save(book));
                })
                .orElse(null);
    }

    @Transactional
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BookDTO> findByRating(String rating) {
        return bookRepository.findByRating(rating).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findByPublicationYearBetween(Integer startYear, Integer endYear) {
        return bookRepository.findByPublicationYearBetween(startYear, endYear).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findByAuthorName(String name) {
        return bookRepository.findByAuthorName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> searchByKeyword(String keyword) {
        return bookRepository.findByKeywordsContainingIgnoreCase(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO addBookWithNewAuthor(BookDTO bookDTO, AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setCountry(authorDTO.getCountry());
        Author savedAuthor = authorRepository.save(author);

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(savedAuthor);
        book.setKeywords(bookDTO.getKeywords());
        book.setImagePath(bookDTO.getImagePath());
        book.setRating(bookDTO.getRating());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthorId(book.getAuthor().getId());
        dto.setAuthorName(book.getAuthor().getName());
        dto.setKeywords(book.getKeywords());
        dto.setImagePath(book.getImagePath());
        dto.setRating(book.getRating());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        return dto;
    }
}
