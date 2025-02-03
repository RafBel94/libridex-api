package com.rafbel94.libridex_api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rafbel94.libridex_api.entity.Book;
import com.rafbel94.libridex_api.model.BookDTO;
import com.rafbel94.libridex_api.model.BookUpdateDTO;
import com.rafbel94.libridex_api.repository.BookRepository;
import com.rafbel94.libridex_api.service.BookService;

import jakarta.transaction.Transactional;

@Service("bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    @Qualifier("bookRepository")
    private BookRepository bookRepository;

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
