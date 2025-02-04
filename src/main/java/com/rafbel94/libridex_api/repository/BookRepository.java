package com.rafbel94.libridex_api.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rafbel94.libridex_api.entity.Book;

public interface BookRepository extends JpaRepository<Book, Serializable> {
    Book findByTitle(String title);

    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.id <> :id")
    Book findByTitleAndNotId(@Param("title") String title, @Param("id") Integer id);
    
    void deleteById(Integer id);

    @Query("SELECT b FROM Book b WHERE (:genres IS NULL OR b.genre IN :genres) AND (:authors IS NULL OR b.author IN :authors) AND (:beforePublishingDateParsed IS NULL OR b.publishingDate <= :beforePublishingDateParsed) AND (:afterPublishingDateParsed IS NULL OR b.publishingDate >= :afterPublishingDateParsed)")
    List<Book> findByFilters(List<String> genres, List<String> authors, LocalDate beforePublishingDateParsed,
            LocalDate afterPublishingDateParsed);
}
