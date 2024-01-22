package com.project.devtogether.project.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public record ProjectRegisterRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank List<String> skills,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDateTime advertisementEndDate
) {
}
