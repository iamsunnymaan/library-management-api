package com.example.library.loan.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BorrowRequest(
        @NotNull(message = "Book id is required.")
        Long bookId,

        @NotNull(message = "Member id is required.")
        Long memberId,

        @Min(value = 1, message = "Loan days must be at least 1.")
        Integer loanDays
) {
}
