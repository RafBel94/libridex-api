package com.rafbel94.libridex_api.controller.API;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafbel94.libridex_api.service.BookService;

@RestController
@RequestMapping("/api/books")
public class RestBook {

    @Autowired
    @Qualifier("bookService")
    private BookService bookService;

    @SuppressWarnings("null") // When bindingResult is not null, it is always used
    @GetMapping("/get")
    public ResponseEntity<?> getBooks(
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) List<String> authors, @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String beforePublishingDate,
            @RequestParam(required = false) String afterPublishingDate) {
        BindingResult bindingResult = null;

        if (!bookService.isFindByFiltersValid(genres, authors, sortBy, beforePublishingDate, afterPublishingDate,
                bindingResult)) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.unprocessableEntity().body(errors);
        }

        return ResponseEntity
                .ok(bookService.findByFilters(genres, authors, sortBy, beforePublishingDate, afterPublishingDate));
    }
}
