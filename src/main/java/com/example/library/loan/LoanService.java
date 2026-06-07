package com.example.library.loan;

import com.example.library.book.Book;
import com.example.library.book.BookService;
import com.example.library.common.BadRequestException;
import com.example.library.common.NotFoundException;
import com.example.library.member.Member;
import com.example.library.member.MemberService;
import com.example.library.member.MemberStatus;
import com.example.library.loan.dto.BorrowRequest;
import com.example.library.loan.dto.LoanResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final int defaultLoanDays;

    public LoanService(
            LoanRepository loanRepository,
            BookService bookService,
            MemberService memberService,
            @Value("${library.loan.default-days:14}") int defaultLoanDays
    ) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.memberService = memberService;
        this.defaultLoanDays = defaultLoanDays;
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findLoans(LoanStatus status, Boolean overdue) {
        List<Loan> loans;
        if (Boolean.TRUE.equals(overdue)) {
            loans = loanRepository.findByStatusAndDueDateBefore(LoanStatus.BORROWED, LocalDate.now());
        } else if (status != null) {
            loans = loanRepository.findByStatus(status);
        } else {
            loans = loanRepository.findAll();
        }

        return loans.stream()
                .map(LoanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoanResponse getLoan(Long id) {
        return LoanResponse.from(getLoanEntity(id));
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> getActiveLoansForMember(Long memberId) {
        return loanRepository.findByMemberIdAndStatus(memberId, LoanStatus.BORROWED)
                .stream()
                .map(LoanResponse::from)
                .toList();
    }

    public LoanResponse borrowBook(BorrowRequest request) {
        Book book = bookService.getBookEntity(request.bookId());
        Member member = memberService.getMemberEntity(request.memberId());
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BadRequestException("Suspended members cannot borrow books.");
        }

        book.checkoutCopy();
        int loanDays = request.loanDays() == null ? defaultLoanDays : request.loanDays();
        LocalDate borrowedDate = LocalDate.now();
        Loan loan = new Loan(book, member, borrowedDate, borrowedDate.plusDays(loanDays));
        return LoanResponse.from(loanRepository.save(loan));
    }

    public LoanResponse returnBook(Long loanId) {
        Loan loan = getLoanEntity(loanId);
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BadRequestException("Loan is already returned.");
        }
        loan.markReturned(LocalDate.now());
        loan.getBook().returnCopy();
        return LoanResponse.from(loan);
    }

    public Loan getLoanEntity(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found with id: " + id));
    }
}
