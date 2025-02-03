package com.rafbel94.libridex_api.service;

import java.util.List;

import com.rafbel94.libridex_api.entity.Book;
import com.rafbel94.libridex_api.model.BookDTO;
import com.rafbel94.libridex_api.model.BookUpdateDTO;

public interface BookService {
    Book findById(Integer id);
    Book addBook(Book book);
    void deleteById(Integer id);
    List<Book> getAllBooks();
    List<String> validateBookCreation(BookDTO bookDTO);
    List<String> validateBookUpdate(BookUpdateDTO bookUpdateDTO);
    BookDTO toDTO(Book book);
    Book toEntity(BookDTO bookDTO);
    Book toEntity(BookUpdateDTO bookUpdateDTO);
}
