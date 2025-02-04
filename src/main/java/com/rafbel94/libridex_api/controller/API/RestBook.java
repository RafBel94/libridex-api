package com.rafbel94.libridex_api.controller.API;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<?> addBook(@RequestHeader("Authorization") String token, @Valid @RequestBody BookDTO bookDTO) {
        Map<String, Object> response = new HashMap<>();
        if (token == null){
            response.put("error", "An authentication token is mandatory");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } 

        List<String> errors = bookService.validateBookCreation(bookDTO);
        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        bookDTO.setCreatedAt(LocalDateTime.now());
        bookService.addBook(bookService.toEntity(bookDTO));
        return ResponseEntity.ok(bookDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@RequestHeader("Authorization") String token, @PathVariable Integer id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        Map<String, Object> response = new HashMap<>();
        if (token == null){
            response.put("error", "An authentication token is mandatory");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } 

        bookUpdateDTO.setId(id);
        List<String> errors = bookService.validateBookUpdate(bookUpdateDTO);

        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        bookService.addBook(bookService.toEntity(bookUpdateDTO));
        Book updatedBook = bookService.findById(id);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllBooks(@RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        if (token == null){
            response.put("error", "An authentication token is mandatory");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } 

        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        System.out.println(token);
        if (token == null){
            response.put("error", "An authentication token is mandatory");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } 

        Book book = bookService.findById(id);
        if (book == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(book);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        if (token == null){
            response.put("error", "An authentication token is mandatory");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } 

        Book book = bookService.findById(id);
        if (book != null) {
            bookService.deleteById(id);
            response.put("message", "Book deleted successfully");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> getBooks(@RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) List<String> authors, @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String beforePublishingDate,
            @RequestParam(required = false) String afterPublishingDate) {

            BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "bookFilters");
        if (!bookService.isFindByFiltersValid(genres, authors, sortBy, beforePublishingDate, afterPublishingDate, bindingResult)) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.unprocessableEntity().body(errors);
        }
        List<Book> books = bookService.findByFilters(genres, authors, sortBy, beforePublishingDate, afterPublishingDate);
        if (books.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(books);
    }

}
