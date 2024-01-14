package com.project.devtogether.project.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectUpdateRequest(
        @NotBlank String content,
        @NotBlank String status
) {
}
