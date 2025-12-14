package com.example.library.repository;

import com.example.library.entity.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorJpaRepository extends CrudRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE a.country = :country")
    List<Author> findByCountry(@Param("country") String country);

    List<Author> findByNameContainingIgnoreCase(String name);
}
