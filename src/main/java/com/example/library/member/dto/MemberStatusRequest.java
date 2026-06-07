package com.example.library.member.dto;

import com.example.library.member.MemberStatus;
import jakarta.validation.constraints.NotNull;

public record MemberStatusRequest(
        @NotNull(message = "Status is required.")
        MemberStatus status
) {
}
