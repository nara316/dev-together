package com.project.devtogether.project.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ProjectRegisterRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank List<String> skills
) {
}
