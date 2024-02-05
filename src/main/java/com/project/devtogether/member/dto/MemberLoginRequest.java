package com.project.devtogether.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank String email,
        @NotBlank String password) {

}
