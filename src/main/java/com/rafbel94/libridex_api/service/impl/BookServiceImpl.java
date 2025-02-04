package com.rafbel94.libridex_api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.rafbel94.libridex_api.entity.Book;
import com.rafbel94.libridex_api.model.BookDTO;
import com.rafbel94.libridex_api.model.BookUpdateDTO;
import com.rafbel94.libridex_api.repository.BookRepository;
import com.rafbel94.libridex_api.service.BookService;

import jakarta.transaction.Transactional;

@Service("bookService")
public class BookServiceImpl implements BookService {
    private static final String DEFAULT_SORT = "title_asc";
    private static final String SORTING_REGEX = "title.*|author.*|genre.*|publishingDate.*|createdAt.*";

    @Autowired
    @Qualifier("bookRepository")
    private BookRepository bookRepository;

    @Override
    public List<Book> findByFilters(List<String> genres, List<String> authors, String sortBy,
            String beforePublishingDate, String afterPublishingDate) {

        LocalDate beforePublishingDateParsed = beforePublishingDate != null ? LocalDate.parse(beforePublishingDate) : null;
        LocalDate afterPublishingDateParsed = afterPublishingDate != null ? LocalDate.parse(afterPublishingDate) : null;

        List<Book> books = bookRepository.findByFilters(genres, authors, beforePublishingDateParsed, afterPublishingDateParsed);

        if (sortBy == null || !sortBy.matches(SORTING_REGEX)) {
            sortBy = DEFAULT_SORT;
        }

        String[] sortOptions = sortBy.split("_");
        String sortField = sortOptions[0];
        boolean ascending = sortOptions[1].equals("asc");

        return books.stream()
            .sorted((book1, book2) -> {
                int comparison = 0;
                switch (sortField) {
                    case "title" -> comparison = book1.getTitle().compareTo(book2.getTitle());
                    case "author" -> comparison = book1.getAuthor().compareTo(book2.getAuthor());
                    case "genre" -> comparison = book1.getGenre().compareTo(book2.getGenre());
                    case "publishingDate" -> comparison = book1.getPublishingDate().compareTo(book2.getPublishingDate());
                    case "createdAt" -> comparison = book1.getCreatedAt().compareTo(book2.getCreatedAt());
                    default -> comparison = book1.getTitle().compareTo(book2.getTitle());
                }
                return ascending ? comparison : -comparison;
            })
            .toList();
    }

    @Override
    public boolean isFindByFiltersValid(List<String> genres, List<String> authors, String sortBy,
            String beforePublishingDate, String afterPublishingDate, BindingResult bindingResult) {
        // Check if before date is in valid format
        if (beforePublishingDate != null) {
            try {
                LocalDate.parse(beforePublishingDate);
            } catch (Exception e) {
                bindingResult.rejectValue("beforePublishingDate", HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                        "Invalid date format");
            }
        }
        // Check if after date is valid
        if (afterPublishingDate != null) {
            try {
                LocalDate.parse(afterPublishingDate);
            } catch (Exception e) {
                bindingResult.rejectValue("afterPublishingDate", HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                        "Invalid date format");
            }
        }
        // Check if before date is before after date
        if (afterPublishingDate != null && beforePublishingDate != null && LocalDate.parse(beforePublishingDate).isAfter(LocalDate.parse(afterPublishingDate))) {
            bindingResult.rejectValue("beforePublishingDate", HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                    "Before date must be before after date");
        }
        // Check if sort format is valid
        if (sortBy != null) {
            String[] sortOptions = sortBy.split("_");
            if (!sortOptions[1].equals("asc") && !sortOptions[1].equals("desc") || sortOptions.length > 2) {
                bindingResult.rejectValue("sortBy", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Invalid sort format");
            }
        }
        // Check if sort field is valid
        if (sortBy != null && !sortBy.matches(SORTING_REGEX)) {
            bindingResult.rejectValue("sortBy", HttpStatus.UNPROCESSABLE_ENTITY.toString(), "Invalid sort field");
        }
        return !bindingResult.hasErrors();
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book addBook(Book book) {
        System.out.println(book);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<String> validateBookCreation(BookDTO bookDTO) {
        List<String> errors = new ArrayList<>();

        if (bookRepository.findByTitle(bookDTO.getTitle()) != null) {
            errors.add("There's already a book with that title");
            return errors;
        }
        if (!bookDTO.getImage().startsWith("http://") && !bookDTO.getImage().startsWith("https://"))
            errors.add("The image must be a valid URL");

        return errors;
    }

    @Override
    public List<String> validateBookUpdate(BookUpdateDTO bookUpdateDTO) {
        Book book = bookRepository.findById(bookUpdateDTO.getId()).orElse(null);
        List<String> errors = new ArrayList<>();

        if (isRequestBodyEmpty(bookUpdateDTO)){
            errors.add("Request body must not be empty");
            return errors;
        }

        if (book == null) {
            errors.add("There's no book with that id");
            return errors;
        }

        if (bookUpdateDTO.getTitle() != null) {
            if (bookRepository.findByTitleAndNotId(bookUpdateDTO.getTitle(), bookUpdateDTO.getId()) != null) {
                errors.add("There's already a book with that title");
                return errors;
            }
        } else if (bookUpdateDTO.getPublishingDate() != null)
            if (!bookUpdateDTO.getImage().startsWith("http://")
                    && !bookUpdateDTO.getImage().startsWith("https://"))
                errors.add("The image must be a valid URL");

        return errors;
    }

    @Override
    public boolean isRequestBodyEmpty(BookUpdateDTO bookUpdateDTO) {
        return (bookUpdateDTO.getTitle() == null &&
                bookUpdateDTO.getAuthor() == null &&
                bookUpdateDTO.getGenre() == null &&
                bookUpdateDTO.getPublishingDate() == null);
    }

    // MODEL MAPPERS
    @Override
    public BookDTO toDTO(Book book) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(book, BookDTO.class);
    }

    @Override
    public Book toEntity(BookDTO bookDTO) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(bookDTO, Book.class);
    }

    @Override
    public Book toEntity(BookUpdateDTO bookUpdateDTO) {
        Book existingBook = bookRepository.findById(bookUpdateDTO.getId()).orElse(null);

        if (bookUpdateDTO.getTitle() != null) {
            existingBook.setTitle(bookUpdateDTO.getTitle());
        }
        if (bookUpdateDTO.getImage() != null) {
            existingBook.setImage(bookUpdateDTO.getImage());
        }
        if (bookUpdateDTO.getAuthor() != null) {
            existingBook.setAuthor(bookUpdateDTO.getAuthor());
        }
        if (bookUpdateDTO.getGenre() != null) {
            existingBook.setGenre(bookUpdateDTO.getGenre());
        }
        if (bookUpdateDTO.getPublishingDate() != null) {
            existingBook.setPublishingDate(bookUpdateDTO.getPublishingDate());
        }

        existingBook.setLent(bookUpdateDTO.isLent());

        return existingBook;
    }
}
