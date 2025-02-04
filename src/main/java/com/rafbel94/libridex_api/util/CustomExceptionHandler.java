package com.rafbel94.libridex_api.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

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
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Map<String, String>> handleInvalidFormatException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
            errors.put(fieldName, "The publishing date must follow the format yyyy-MM-dd");
        } else {
            errors.put("error", "Invalid request body.");
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Map<String, String>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Missing required header: " + ex.getHeaderName());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
