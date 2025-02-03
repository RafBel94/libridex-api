package com.rafbel94.libridex_api.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.rafbel94.libridex_api.entity.Book;

public interface BookService {
    boolean isFindByFiltersValid(List<String> genres, List<String> authors, String sortBy, String beforePublishingDate, String afterPublishingDate, BindingResult bindingResult);
    List<Book> findByFilters(List<String> genres, List<String> authors, String sortBy, String beforePublishingDate, String afterPublishingDate);
}
