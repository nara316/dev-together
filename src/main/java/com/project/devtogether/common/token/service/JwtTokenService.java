package com.project.devtogether.common.token.service;

import com.project.devtogether.common.error.MemberErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.common.token.privider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenDto reissueToken(String refreshToken) {
        // Refresh Token 검증
        jwtTokenProvider.validateToken(refreshToken);

        // RefreshToken 에서 redis key값을 가져옴
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if(!redisRefreshToken.equals(refreshToken)) {
            throw new ApiException(MemberErrorCode.TOKEN_NOT_EXIST);
        }

        // access 토큰 재발행
        TokenDto tokenDto = new TokenDto(
                jwtTokenProvider.issueToken(authentication),
                refreshToken
        );

        return tokenDto;
    }
}
