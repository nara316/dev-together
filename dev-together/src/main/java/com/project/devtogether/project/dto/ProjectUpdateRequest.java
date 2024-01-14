package com.project.devtogether.project.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ProjectUpdateRequest(
        @NotBlank String content,
        @NotBlank String status,
        @NotBlank List<String> skills
        ) {
}
