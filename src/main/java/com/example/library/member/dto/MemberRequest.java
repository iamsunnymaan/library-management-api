package com.example.library.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRequest(
        @NotBlank(message = "Name is required.")
        @Size(max = 120, message = "Name must be 120 characters or fewer.")
        String name,

        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be valid.")
        @Size(max = 160, message = "Email must be 160 characters or fewer.")
        String email,

        @Size(max = 30, message = "Phone must be 30 characters or fewer.")
        String phone
) {
}
