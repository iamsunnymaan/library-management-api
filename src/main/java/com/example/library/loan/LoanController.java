package com.example.library.loan;

import com.example.library.loan.dto.BorrowRequest;
import com.example.library.loan.dto.LoanResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public List<LoanResponse> findLoans(
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(required = false) Boolean overdue
    ) {
        return loanService.findLoans(status, overdue);
    }

    @GetMapping("/{id}")
    public LoanResponse getLoan(@PathVariable Long id) {
        return loanService.getLoan(id);
    }

    @PostMapping("/borrow")
    public ResponseEntity<LoanResponse> borrowBook(@Valid @RequestBody BorrowRequest request) {
        LoanResponse response = loanService.borrowBook(request);
        return ResponseEntity.created(URI.create("/api/loans/" + response.id())).body(response);
    }

    @PostMapping("/{id}/return")
    public LoanResponse returnBook(@PathVariable Long id) {
        return loanService.returnBook(id);
    }
}
