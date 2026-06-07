package com.example.library.member;

import com.example.library.loan.LoanService;
import com.example.library.loan.dto.LoanResponse;
import com.example.library.member.dto.MemberRequest;
import com.example.library.member.dto.MemberResponse;
import com.example.library.member.dto.MemberStatusRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final LoanService loanService;

    public MemberController(MemberService memberService, LoanService loanService) {
        this.memberService = memberService;
        this.loanService = loanService;
    }

    @GetMapping
    public List<MemberResponse> findMembers() {
        return memberService.findMembers();
    }

    @GetMapping("/{id}")
    public MemberResponse getMember(@PathVariable Long id) {
        return memberService.getMember(id);
    }

    @GetMapping("/{id}/loans/active")
    public List<LoanResponse> getActiveLoans(@PathVariable Long id) {
        memberService.getMember(id);
        return loanService.getActiveLoansForMember(id);
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/api/members/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public MemberResponse updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequest request) {
        return memberService.updateMember(id, request);
    }

    @PatchMapping("/{id}/status")
    public MemberResponse updateStatus(@PathVariable Long id, @Valid @RequestBody MemberStatusRequest request) {
        return memberService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
