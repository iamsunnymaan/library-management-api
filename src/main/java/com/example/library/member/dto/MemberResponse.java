package com.example.library.member.dto;

import com.example.library.member.Member;
import com.example.library.member.MemberStatus;
import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String phone,
        MemberStatus status,
        LocalDateTime createdAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getStatus(),
                member.getCreatedAt()
        );
    }
}
