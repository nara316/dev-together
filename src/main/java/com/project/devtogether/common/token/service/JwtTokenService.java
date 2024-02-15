package com.project.devtogether.common.token.service;

import com.project.devtogether.common.error.MemberErrorCode;
import com.project.devtogether.common.exception.ApiException;
import com.project.devtogether.common.redis.service.RedisService;
import com.project.devtogether.common.security.user.CustomUserDetail;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.common.token.privider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public TokenDto reissueToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        jwtTokenProvider.validateToken(refreshToken);
        //redis Key 가져와야함
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        String email = userDetail.getUsername();
        String redisRefreshToken = redisService.getValues(email);

        if (redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)) {
            TokenDto tokenDto = new TokenDto(
                    jwtTokenProvider.issueToken(authentication),
                    refreshToken
            );
            return tokenDto;
        } else throw new ApiException(MemberErrorCode.TOKEN_IS_NOT_SAME);
    }
}
