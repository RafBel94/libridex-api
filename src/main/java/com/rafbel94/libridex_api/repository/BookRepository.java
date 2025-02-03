package com.rafbel94.libridex_api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rafbel94.libridex_api.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE (:genres IS NULL OR b.genre IN :genres) AND (:authors IS NULL OR b.author IN :authors) AND (:startDate IS NULL OR b.publishingDate >= :startDate) AND (:endDate IS NULL OR b.publishingDate <= :endDate) ORDER BY :sort :order")
    List<Book> findAllWithFilters(List<String> genres, List<String> authors, LocalDate startDate, LocalDate endDate, String sort, String order);
    
}
