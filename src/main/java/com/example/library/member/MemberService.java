package com.example.library.member;

import com.example.library.common.ConflictException;
import com.example.library.common.NotFoundException;
import com.example.library.loan.LoanRepository;
import com.example.library.loan.LoanStatus;
import com.example.library.member.dto.MemberRequest;
import com.example.library.member.dto.MemberResponse;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public MemberService(MemberRepository memberRepository, LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> findMembers() {
        return memberRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResponse getMember(Long id) {
        return MemberResponse.from(getMemberEntity(id));
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new ConflictException("A member with this email already exists.");
        }
        Member member = new Member(
                request.name().trim(),
                request.email().trim().toLowerCase(),
                normalizeOptional(request.phone())
        );
        return MemberResponse.from(memberRepository.save(member));
    }

    public MemberResponse updateMember(Long id, MemberRequest request) {
        Member member = getMemberEntity(id);
        String normalizedEmail = request.email().trim().toLowerCase();
        if (memberRepository.existsByEmailAndIdNot(normalizedEmail, id)) {
            throw new ConflictException("A member with this email already exists.");
        }
        member.setName(request.name().trim());
        member.setEmail(normalizedEmail);
        member.setPhone(normalizeOptional(request.phone()));
        return MemberResponse.from(member);
    }

    public MemberResponse updateStatus(Long id, MemberStatus status) {
        Member member = getMemberEntity(id);
        member.setStatus(status);
        return MemberResponse.from(member);
    }

    public void deleteMember(Long id) {
        Member member = getMemberEntity(id);
        if (loanRepository.existsByMemberIdAndStatus(id, LoanStatus.BORROWED)) {
            throw new ConflictException("Cannot delete a member with active loans.");
        }
        memberRepository.delete(member);
    }

    public Member getMemberEntity(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + id));
    }

    private String normalizeOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
