package com.example.library.loan.dto;

import com.example.library.loan.Loan;
import com.example.library.loan.LoanStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long memberId,
        String memberName,
        LocalDate borrowedDate,
        LocalDate dueDate,
        LocalDate returnedDate,
        LoanStatus status,
        boolean overdue,
        LocalDateTime createdAt
) {
    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getMember().getId(),
                loan.getMember().getName(),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate(),
                loan.getStatus(),
                loan.isOverdue(LocalDate.now()),
                loan.getCreatedAt()
        );
    }
}
