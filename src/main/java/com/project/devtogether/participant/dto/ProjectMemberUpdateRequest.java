package com.project.devtogether.participant.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectMemberUpdateRequest(
        @NotBlank
        String comment
) {
}
