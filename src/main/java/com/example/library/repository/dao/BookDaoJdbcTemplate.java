package com.example.library.repository.dao;

import com.example.library.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate-based implementation of BookDao.
 * Demonstrates traditional Spring JDBC approach with JdbcTemplate.
 */
@Repository
@RequiredArgsConstructor
public class BookDaoJdbcTemplate implements BookDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Book> BOOK_ROW_MAPPER = (rs, rowNum) -> Book.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .authorId(rs.getLong("author_id"))
            .authorName(rs.getString("author_name"))
            .keywords(rs.getString("keywords"))
            .imagePath(rs.getString("image_path"))
            .rating(rs.getString("rating"))
            .publicationYear(rs.getObject("publication_year", Integer.class))
            .isbn(rs.getString("isbn"))
            .build();

    private static final RowMapper<Book> BOOK_SIMPLE_ROW_MAPPER = (rs, rowNum) -> Book.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .authorId(rs.getLong("author_id"))
            .keywords(rs.getString("keywords"))
            .imagePath(rs.getString("image_path"))
            .rating(rs.getString("rating"))
            .publicationYear(rs.getObject("publication_year", Integer.class))
            .isbn(rs.getString("isbn"))
            .build();

    @Override
    public Long create(Book book) {
        String sql = """
            INSERT INTO books (title, author_id, keywords, image_path, rating, publication_year, isbn)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setLong(2, book.getAuthorId());
            ps.setString(3, book.getKeywords());
            ps.setString(4, book.getImagePath());
            ps.setString(5, book.getRating() != null ? book.getRating() : "0+");
            ps.setObject(6, book.getPublicationYear());
            ps.setString(7, book.getIsbn());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = """
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            WHERE b.id = ?
            """;

        List<Book> results = jdbcTemplate.query(sql, BOOK_ROW_MAPPER, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<Book> findAll() {
        String sql = """
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            ORDER BY b.id
            """;

        return jdbcTemplate.query(sql, BOOK_ROW_MAPPER);
    }

    @Override
    public boolean update(Book book) {
        String sql = """
            UPDATE books
            SET title = ?, author_id = ?, keywords = ?, image_path = ?,
                rating = ?, publication_year = ?, isbn = ?
            WHERE id = ?
            """;

        int rowsAffected = jdbcTemplate.update(sql,
                book.getTitle(),
                book.getAuthorId(),
                book.getKeywords(),
                book.getImagePath(),
                book.getRating(),
                book.getPublicationYear(),
                book.getIsbn(),
                book.getId());

        return rowsAffected > 0;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    @Override
    public List<Book> findByTitleContaining(String title) {
        String sql = """
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            WHERE LOWER(b.title) LIKE LOWER(?)
            ORDER BY b.title
            """;

        return jdbcTemplate.query(sql, BOOK_ROW_MAPPER, "%" + title + "%");
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        String sql = """
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            WHERE b.author_id = ?
            ORDER BY b.publication_year DESC
            """;

        return jdbcTemplate.query(sql, BOOK_ROW_MAPPER, authorId);
    }

    @Override
    public List<Book> findByRating(String rating) {
        String sql = """
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            WHERE b.rating = ?
            ORDER BY b.title
            """;

        return jdbcTemplate.query(sql, BOOK_ROW_MAPPER, rating);
    }

    @Override
    public List<Book> findByKeyword(String keyword) {
        String sql = """
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            WHERE LOWER(b.keywords) LIKE LOWER(?)
            ORDER BY b.title
            """;

        return jdbcTemplate.query(sql, BOOK_ROW_MAPPER, "%" + keyword + "%");
    }

    @Override
    public List<Book> search(String title, Long authorId, String keyword) {
        StringBuilder sql = new StringBuilder("""
            SELECT b.id, b.title, b.author_id, a.name as author_name,
                   b.keywords, b.image_path, b.rating, b.publication_year, b.isbn
            FROM books b
            JOIN authors a ON b.author_id = a.id
            WHERE 1=1
            """);

        List<Object> params = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            sql.append(" AND LOWER(b.title) LIKE LOWER(?)");
            params.add("%" + title + "%");
        }

        if (authorId != null) {
            sql.append(" AND b.author_id = ?");
            params.add(authorId);
        }

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND LOWER(b.keywords) LIKE LOWER(?)");
            params.add("%" + keyword + "%");
        }

        sql.append(" ORDER BY b.title");

        return jdbcTemplate.query(sql.toString(), BOOK_ROW_MAPPER, params.toArray());
    }
}
