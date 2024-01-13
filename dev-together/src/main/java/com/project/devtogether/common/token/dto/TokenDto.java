package com.project.devtogether.common.token.dto;

import java.time.LocalDateTime;

public record TokenDto(
        String token,
        LocalDateTime expiredAt
) {
    public static TokenDto of(String jwtToken, LocalDateTime expiredDateTime) {
        return new TokenDto(
                jwtToken,
                expiredDateTime
        );
    }
}
