package com.example.library.loan;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByMemberIdAndStatus(Long memberId, LoanStatus status);

    List<Loan> findByBookIdAndStatus(Long bookId, LoanStatus status);

    List<Loan> findByStatusAndDueDateBefore(LoanStatus status, LocalDate date);

    boolean existsByMemberIdAndStatus(Long memberId, LoanStatus status);
}
