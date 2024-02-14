package com.project.devtogether.common.token.dto;

import java.time.LocalDateTime;

public record TokenDto(
        String accessToken,
        String refreshToken
) {
    public static TokenDto of(String accessToken, String refreshToken) {
        return new TokenDto(
                accessToken,
                refreshToken
        );
    }
}
