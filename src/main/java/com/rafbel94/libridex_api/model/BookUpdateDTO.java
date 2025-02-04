package com.rafbel94.libridex_api.model;

import java.time.LocalDate;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookUpdateDTO {
    Integer id;

    @Size(max = 50, message = "The title must not be more than 50 characters long")
    private String title;

    private String image;

    @Size(max = 30, message = "The author must not be more than 30 characters long")
    private String author;

    @Size(max = 20, message = "The genre must not be more than 20 characters long")
    private String genre;

    @PastOrPresent(message = "The date must be one before today")
    private LocalDate publishingDate;
}
