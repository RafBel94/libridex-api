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

    /**
     * Finds a book by its ID.
     *
     * @param id the ID of the book to be found
     * @return the book if found, or null if not found
     */
    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Finds a book by its title.
     *
     * @param title the title of the book to be found
     * @return the book if found, or null if not found
     */
    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    /**
     * Adds a new book to the repository.
     *
     * @param book the book to be added
     * @return the added book
     */
    @Override
    public Book addBook(Book book) {
        System.out.println(book);
        return bookRepository.save(book);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to be deleted
     */
    @Override
    @Transactional
    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    /**
     * Retrieves all books from the repository.
     *
     * @return the list of all books
     */
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Validates the book creation data that could not be validated through jakarta validations.
     * It looks for books for the same title and for invalid image URLs
     *
     * @param bookDTO the book data transfer object containing the details of the book to be validated
     * @return the list of validation errors, or an empty list if validation is successful
     */
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

    /**
     * Validates the book update data that could not be validated through jakarta validations.
     * It looks for empty request bodies, inexistent books, books with same title and invalid image URLs
     *
     * @param bookUpdateDTO the book update data transfer object containing the updated details of the book to be validated
     * @return the list of validation errors, or an empty list if validation is successful
     */
    @Override
    public List<String> validateBookUpdate(BookUpdateDTO bookUpdateDTO) {
        Book book = bookRepository.findById(bookUpdateDTO.getId()).orElse(null);
        List<String> errors = new ArrayList<>();

        if (isRequestBodyEmpty(bookUpdateDTO)) {
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

    /**
     * Checks if the book update request body is empty.
     *
     * @param bookUpdateDTO the book update data transfer object to be checked
     * @return true if the request body is empty, false otherwise
     */
    @Override
    public boolean isRequestBodyEmpty(BookUpdateDTO bookUpdateDTO) {
        return (bookUpdateDTO.getTitle() == null &&
                bookUpdateDTO.getAuthor() == null &&
                bookUpdateDTO.getGenre() == null &&
                bookUpdateDTO.getPublishingDate() == null);
    }

    /**
     * Finds books based on various filters.
     *
     * @param genres               the list of genres to filter by (optional)
     * @param authors              the list of authors to filter by (optional)
     * @param sortBy               the field to sort the results by (optional)
     * @param beforePublishingDate the upper limit for the publishing date filter (optional)
     * @param afterPublishingDate  the lower limit for the publishing date filter (optional)
     * @return the list of books that match the filters
     */
    @Override
    public List<Book> findByFilters(List<String> genres, List<String> authors, String sortBy,
            String beforePublishingDate, String afterPublishingDate) {

        LocalDate beforePublishingDateParsed = beforePublishingDate != null ? LocalDate.parse(beforePublishingDate)
                : null;
        LocalDate afterPublishingDateParsed = afterPublishingDate != null ? LocalDate.parse(afterPublishingDate) : null;

        List<Book> books = bookRepository.findByFilters(genres, authors, beforePublishingDateParsed,
                afterPublishingDateParsed);

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
                        case "publishingDate" ->
                            comparison = book1.getPublishingDate().compareTo(book2.getPublishingDate());
                        case "createdAt" -> comparison = book1.getCreatedAt().compareTo(book2.getCreatedAt());
                        default -> comparison = book1.getTitle().compareTo(book2.getTitle());
                    }
                    return ascending ? comparison : -comparison;
                })
                .toList();
    }

    /**
     * Validates the filters for finding books.
     *
     * @param genres               the list of genres to filter by (optional)
     * @param authors              the list of authors to filter by (optional)
     * @param sortBy               the field to sort the results by (optional)
     * @param beforePublishingDate the upper limit for the publishing date filter (optional)
     * @param afterPublishingDate  the lower limit for the publishing date filter (optional)
     * @param bindingResult        the binding result to hold validation errors
     * @return true if the filters are valid, false otherwise
     */
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
        if (afterPublishingDate != null && beforePublishingDate != null
                && LocalDate.parse(beforePublishingDate).isAfter(LocalDate.parse(afterPublishingDate))) {
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
