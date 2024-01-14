package com.project.devtogether.project.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRegisterRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}
