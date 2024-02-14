package com.project.devtogether.common.token.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.common.token.privider.JwtTokenProvider;
import com.project.devtogether.common.token.service.JwtTokenService;
import com.project.devtogether.member.dto.MemberLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenService jwtTokenService;

    @Operation(summary = "access token 재발행 API")
    @PatchMapping("/reissue")
    public Api<TokenDto> reissue(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        TokenDto tokenDto = jwtTokenService.reissueToken(refreshToken);
        return Api.OK(tokenDto);
    }
}
