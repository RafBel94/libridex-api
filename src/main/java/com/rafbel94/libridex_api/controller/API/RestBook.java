package com.rafbel94.libridex_api.controller.API;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafbel94.libridex_api.entity.Book;
import com.rafbel94.libridex_api.model.BookDTO;
import com.rafbel94.libridex_api.model.BookUpdateDTO;
import com.rafbel94.libridex_api.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class RestBook {

    @Autowired
    @Qualifier("bookService")
    private BookService bookService;

    @PostMapping("")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        List<String> errors = bookService.validateBookCreation(bookDTO);
        Map<String, Object> response = new HashMap<>();
        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        bookDTO.setCreatedAt(LocalDateTime.now());
        bookService.addBook(bookService.toEntity(bookDTO));
        return ResponseEntity.ok(bookDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        bookUpdateDTO.setId(id);
        List<String> errors = bookService.validateBookUpdate(bookUpdateDTO);
        Map<String, Object> response = new HashMap<>();
        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        bookService.addBook(bookService.toEntity(bookUpdateDTO));
        Book updatedBook = bookService.findById(id);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        Book book = bookService.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (book != null) {
            bookService.deleteById(id);
            response.put("message", "Book deleted successfully");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

}
