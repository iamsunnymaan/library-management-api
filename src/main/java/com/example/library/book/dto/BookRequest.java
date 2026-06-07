package com.example.library.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "Title is required.")
        @Size(max = 180, message = "Title must be 180 characters or fewer.")
        String title,

        @NotBlank(message = "Author is required.")
        @Size(max = 120, message = "Author must be 120 characters or fewer.")
        String author,

        @NotBlank(message = "ISBN is required.")
        @Size(max = 20, message = "ISBN must be 20 characters or fewer.")
        String isbn,

        @Size(max = 80, message = "Category must be 80 characters or fewer.")
        String category,

        @Min(value = 1, message = "Total copies must be at least 1.")
        int totalCopies
) {
}
