package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookJpaRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.rating = :rating")
    List<Book> findByRating(@Param("rating") String rating);

    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);

    List<Book> findByAuthorName(String name);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByKeywordsContainingIgnoreCase(String keyword);
}
