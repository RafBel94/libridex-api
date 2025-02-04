package com.rafbel94.libridex_api.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rafbel94.libridex_api.entity.Book;

public interface BookRepository extends JpaRepository<Book, Serializable> {
    Book findByTitle(String title);

    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.id <> :id")
    Book findByTitleAndNotId(@Param("title") String title, @Param("id") Integer id);
    
    void deleteById(Integer id);
}
