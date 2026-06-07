package com.example.library.book.dto;

import com.example.library.book.Book;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        String category,
        int totalCopies,
        int availableCopies,
        boolean available,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getAvailableCopies() > 0,
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
