package com.example.library.repository.dao;

import com.example.library.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JdbcClient-based implementation of AuthorDao.
 * Demonstrates modern Spring 6.1+ approach with fluent JdbcClient API.
 */
@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbcClient implements AuthorDao {

    private final JdbcClient jdbcClient;

    @Override
    public Long create(Author author) {
        String sql = """
            INSERT INTO authors (name, country, birth_year)
            VALUES (:name, :country, :birthYear)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("name", author.getName())
                .param("country", author.getCountry())
                .param("birthYear", author.getBirthYear())
                .update(keyHolder, "id");

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = """
            SELECT id, name, country, birth_year
            FROM authors
            WHERE id = :id
            """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(Author.class)
                .optional();
    }

    @Override
    public List<Author> findAll() {
        String sql = """
            SELECT id, name, country, birth_year
            FROM authors
            ORDER BY name
            """;

        return jdbcClient.sql(sql)
                .query(Author.class)
                .list();
    }

    @Override
    public boolean update(Author author) {
        String sql = """
            UPDATE authors
            SET name = :name, country = :country, birth_year = :birthYear
            WHERE id = :id
            """;

        int rowsAffected = jdbcClient.sql(sql)
                .param("name", author.getName())
                .param("country", author.getCountry())
                .param("birthYear", author.getBirthYear())
                .param("id", author.getId())
                .update();

        return rowsAffected > 0;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM authors WHERE id = :id";

        int rowsAffected = jdbcClient.sql(sql)
                .param("id", id)
                .update();

        return rowsAffected > 0;
    }

    @Override
    public List<Author> findByNameContaining(String name) {
        String sql = """
            SELECT id, name, country, birth_year
            FROM authors
            WHERE LOWER(name) LIKE LOWER(:name)
            ORDER BY name
            """;

        return jdbcClient.sql(sql)
                .param("name", "%" + name + "%")
                .query(Author.class)
                .list();
    }

    @Override
    public List<Author> findByCountry(String country) {
        String sql = """
            SELECT id, name, country, birth_year
            FROM authors
            WHERE LOWER(country) = LOWER(:country)
            ORDER BY name
            """;

        return jdbcClient.sql(sql)
                .param("country", country)
                .query(Author.class)
                .list();
    }

    @Override
    public Optional<Author> findByName(String name) {
        String sql = """
            SELECT id, name, country, birth_year
            FROM authors
            WHERE name = :name
            """;

        return jdbcClient.sql(sql)
                .param("name", name)
                .query(Author.class)
                .optional();
    }
}
