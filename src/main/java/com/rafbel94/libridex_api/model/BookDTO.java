package com.rafbel94.libridex_api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookDTO {

    Integer id;

    @NotBlank(message = "The title is mandatory")
    @Size(max = 50, message = "The title must not be more than 50 characters long")
    private String title;

    @NotBlank(message = "The image is mandatory")
    private String image;

    @NotBlank(message = "The author is mandatory")
    @Size(max = 30, message = "The author must not be more than 30 characters long")
    private String author;

    @NotBlank(message = "The genre is mandatory")
    @Size(max = 20, message = "The genre must not be more than 20 characters long")
    private String genre;

    @NotNull(message = "The publishing date is mandatory")
    @PastOrPresent(message = "The date must be one before today")
    private LocalDate publishingDate;

    private LocalDateTime createdAt;

    boolean lent;
}
