package com.rafbel94.libridex_api.controller.API;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.rafbel94.libridex_api.entity.FetchResponse;
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
    @PostMapping
    public ResponseEntity<FetchResponse> addBook(@Valid @RequestBody BookDTO bookDTO) {
        List<Object> data = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        messages = bookService.validateBookCreation(bookDTO);
        if (!messages.isEmpty()) {
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        messages.add("Book created successfully");

        bookDTO.setCreatedAt(LocalDateTime.now());
        data.add(bookService.addBook(bookService.toEntity(bookDTO)));
        FetchResponse response = new FetchResponse(true, messages, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public ResponseEntity<FetchResponse> updateBook(@PathVariable Integer id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        List<Object> data = new ArrayList<>();

        bookUpdateDTO.setId(id);
        List<String> messages = bookService.validateBookUpdate(bookUpdateDTO);

        if (!messages.isEmpty()) {
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        bookService.addBook(bookService.toEntity(bookUpdateDTO));

        data.add(bookService.findById(id));

        messages.add("Book updated successfully");
        FetchResponse response = new FetchResponse(true, messages, data);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all books from the system.
     *
     * @return a ResponseEntity containing the list of all books if successful, or
     *         an error message if validation fails
     */
    @GetMapping
    public ResponseEntity<FetchResponse> getAllBooks() {
        List<Object> data = new ArrayList<>();
        List<String> messages = new ArrayList<>();

        for (Book book : bookService.getAllBooks()) {
            data.add(book);
        }

        if (data.isEmpty()){
            messages.add("No books found");
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }    

        messages.add("Books retrieved successfully");
        FetchResponse response = new FetchResponse(true, messages, data);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific book by its ID.
     *
     * @param id    the ID of the book to be retrieved
     * @return a ResponseEntity containing the book details if found, or an error
     *         message if validation fails
     */
    @GetMapping("/{id}")
    public ResponseEntity<FetchResponse> getBook(@PathVariable Integer id) {
        List<Object> data = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        Book book = bookService.findById(id);
        
        if (book == null) {
            messages.add("Book with id " + id + " not found");
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        messages.add("Book retrieved successfully");
        data.add(book);
        FetchResponse response = new FetchResponse(true, messages, data);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a specific book by its ID.
     *
     * @param id    the ID of the book to be deleted
     * @return a ResponseEntity containing a success message if the book is deleted,
     *         or an error message if validation fails
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<FetchResponse> deleteBook(@PathVariable Integer id) {
        List<String> messages = new ArrayList<>();
        List<Object> data = new ArrayList<>();

        Book book = bookService.findById(id);
        if (book == null) {
            messages.add("Book with id " + id + " not found");
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        
        bookService.deleteById(id);
        messages.add("Book deleted successfully");
        FetchResponse response = new FetchResponse(true, messages, data);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<FetchResponse> getBooks(
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) List<String> authors, @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String beforePublishingDate,
            @RequestParam(required = false) String afterPublishingDate) {

        List<String> messages = new ArrayList<>();
        List<Object> data = new ArrayList<>();

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "bookFilters");
        if (!bookService.isFindByFiltersValid(genres, authors, sortBy, beforePublishingDate, afterPublishingDate,
                bindingResult)) {
            bindingResult.getFieldErrors().forEach(error -> messages.add(error.getDefaultMessage()));
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        List<Book> books = bookService.findByFilters(genres, authors, sortBy, beforePublishingDate,
                afterPublishingDate);

        for (Book book : books) {
            data.add(book);
        }

        if (books.isEmpty()) {
            messages.add("No books found");
            FetchResponse response = new FetchResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        messages.add("Books retrieved successfully");
        FetchResponse response = new FetchResponse(true, messages, data);
        return ResponseEntity.ok(response);
    }

}
