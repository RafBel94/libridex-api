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

    /**
     * Adds a new book to the system.
     *
     * @param bookDTO the book data transfer object containing the details of the
     *                book to be added
     * @apiNote bookDTO attributes are validated through jakarta validations
     * @return a ResponseEntity containing the added book details if successful, or
     *         an error message if validation fails
     */
    @PostMapping("")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        Map<String, Object> response = new HashMap<>();

        List<String> errors = bookService.validateBookCreation(bookDTO);
        if (!errors.isEmpty()) {
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        bookDTO.setCreatedAt(LocalDateTime.now());
        Book createdBook = bookService.addBook(bookService.toEntity(bookDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Updates an existing book in the system.
     *
     * @param id            the ID of the book to be updated
     * @param bookUpdateDTO the book update data transfer object containing the
     *                      updated details of the book
     * @apiNote bookUpdateDTO attributes are validated through jakarta validations
     * @return a ResponseEntity containing the updated book details if successful,
     *         or an error message if validation fails
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        Map<String, Object> response = new HashMap<>();

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

    /**
     * Retrieves all books from the system.
     *
     * @return a ResponseEntity containing the list of all books if successful, or
     *         an error message if validation fails
     */
    @GetMapping("")
    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(books);
    }

    /**
     * Retrieves a specific book by its ID.
     *
     * @param id    the ID of the book to be retrieved
     * @return a ResponseEntity containing the book details if found, or an error
     *         message if validation fails
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Integer id) {
        Book book = bookService.findById(id);
        if (book == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(book);
    }

    /**
     * Deletes a specific book by its ID.
     *
     * @param id    the ID of the book to be deleted
     * @return a ResponseEntity containing a success message if the book is deleted,
     *         or an error message if validation fails
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        Book book = bookService.findById(id);
        if (book != null) {
            bookService.deleteById(id);
            response.put("message", "Book deleted successfully");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Searches for books based on various filters.
     *
     * @param genres               the list of genres to filter by (optional)
     * @param authors              the list of authors to filter by (optional)
     * @param sortBy               the field to sort the results by (optional)
     * @param beforePublishingDate the upper limit for the publishing date filter
     *                             (optional)
     * @param afterPublishingDate  the lower limit for the publishing date filter
     *                             (optional)
     * @return a ResponseEntity containing the list of books that match the filters
     *         if successful, or an error message if validation fails
     */
    @GetMapping("/search")
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) List<String> authors, @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String beforePublishingDate,
            @RequestParam(required = false) String afterPublishingDate) {

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "bookFilters");
        if (!bookService.isFindByFiltersValid(genres, authors, sortBy, beforePublishingDate, afterPublishingDate,
                bindingResult)) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.unprocessableEntity().body(errors);
        }
        List<Book> books = bookService.findByFilters(genres, authors, sortBy, beforePublishingDate,
                afterPublishingDate);
        if (books.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(books);
    }

}
