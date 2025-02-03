package com.rafbel94.libridex_api.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.rafbel94.libridex_api.entity.Book;
import com.rafbel94.libridex_api.repository.BookRepository;
import com.rafbel94.libridex_api.service.BookService;

@Service("bookService")
public class BookServiceImpl implements BookService {

    private static final String DEFAULT_SORT = "title_asc";
    private static final String SORTING_REGEX = "title.*|author.*|genre.*|publishingDate.*|createdAt.*";

    @Autowired
    @Qualifier("bookRepository")
    private BookRepository bookRepository;

    @Override
    public List<Book> findByFilters(List<String> genres, List<String> authors, String sortBy, String beforePublishingDate, String afterPublishingDate) {

        LocalDate beforePublishingDateParsed = beforePublishingDate != null ? LocalDate.parse(beforePublishingDate) : null;
        LocalDate afterPublishingDateParsed = afterPublishingDate != null ? LocalDate.parse(afterPublishingDate) : null;

        String[] sortOptions = sortBy.split("_");
        return bookRepository.findAllWithFilters(genres, authors, beforePublishingDateParsed, afterPublishingDateParsed);
    }

    @Override
    public boolean isFindByFiltersValid(List<String> genres, List<String> authors, String sortBy,
            String beforePublishingDate, String afterPublishingDate, BindingResult bindingResult) {
            // Check if before date is in valid format
            if(beforePublishingDate != null) {
                try {
                    LocalDate.parse(beforePublishingDate);
                } catch (Exception e) {
                    bindingResult.rejectValue("beforePublishingDate", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Invalid date format");
                }
            }
            // Check if after date is valid
            if(afterPublishingDate != null) {
                try {
                    LocalDate.parse(afterPublishingDate);
                } catch (Exception e) {
                    bindingResult.rejectValue("afterPublishingDate", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Invalid date format");
                }
            }
            // Check if before date is before after date
            if(LocalDate.parse(beforePublishingDate).isAfter(LocalDate.parse(afterPublishingDate))) {
                bindingResult.rejectValue("beforePublishingDate", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Before date must be before after date");
            }
            // Check if sort format is valid
            if(sortBy != null) {
                String[] sortOptions = sortBy.split("_");
                if(!sortOptions[1].equals("asc") && !sortOptions[1].equals("desc") || sortOptions.length > 2) {
                    bindingResult.rejectValue("sortBy", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Invalid sort format");
                }
            }
            // Default sorting
            if(sortBy == null || sortBy.split("_").length < 2) {
                sortBy = DEFAULT_SORT;
            }
            // Check if sort field is valid
            if(!sortBy.matches(SORTING_REGEX)) {
                bindingResult.rejectValue("sortBy", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Invalid sort field");
            }
        return !bindingResult.hasErrors();
    }

    
    

}
