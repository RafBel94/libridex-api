package com.rafbel94.libridex_api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rafbel94.libridex_api.entity.AuthResponse;

@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException exceptions and returns a response
     * entity
     * containing a map of field errors and their corresponding error messages.
     *
     * @param ex the MethodArgumentNotValidException exception
     * @return a ResponseEntity containing a map of field names and error messages,
     *         with a BAD_REQUEST (400) HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            messages.add(errorMessage);
        });

        AuthResponse response = new AuthResponse(false, messages, new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpMessageNotReadableException exceptions and returns a response
     * entity containing a map of field errors and their corresponding error
     * messages.
     * 
     * If the exception cause is an InvalidFormatException, it provides a specific
     * error message for the publishing date format.
     *
     * @param ex the HttpMessageNotReadableException exception
     * @return a ResponseEntity containing a map of field names and error messages,
     *         with a BAD_REQUEST (400) HTTP status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AuthResponse> handleInvalidFormatException(HttpMessageNotReadableException ex) {
        List<String> messages = new ArrayList<>();

        if (ex.getCause() instanceof InvalidFormatException) {
            messages.add("The publishing date must follow the format yyyy-MM-dd");
        } else {
            messages.add("Invalid request body.");
        }

        AuthResponse response = new AuthResponse(false, messages, new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MissingRequestHeaderException exceptions and returns a response
     * entity containing a map of error messages.
     *
     * @param ex the MissingRequestHeaderException exception
     * @return a ResponseEntity containing a map of error messages,
     *         with a BAD_REQUEST (400) HTTP status
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<AuthResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        List<String> messages = new ArrayList<>();
        messages.add("Missing required header: " + ex.getHeaderName());

        AuthResponse response = new AuthResponse(false, messages, new HashMap<>());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
