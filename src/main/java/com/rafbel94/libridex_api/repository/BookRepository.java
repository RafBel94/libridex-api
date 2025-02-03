package com.rafbel94.libridex_api.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafbel94.libridex_api.entity.Book;

public interface BookRepository extends JpaRepository<Book, Serializable> {
    Book findByTitle(String title);
    void deleteById(Integer id);
}
