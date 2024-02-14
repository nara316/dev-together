package com.project.devtogether.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ProjectUpdateRequest(
        @NotBlank String content,
        @NotBlank String status,
        @NotEmpty List<String> skills
        ) {
}
