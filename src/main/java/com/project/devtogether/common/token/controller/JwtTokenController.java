package com.project.devtogether.common.token.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.common.token.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtTokenController {

    private final JwtTokenService jwtTokenService;

    @Operation(summary = "access token 재발행 API")
    @PatchMapping("/reissue")
    public Api<TokenDto> reissue(HttpServletRequest request) {
        TokenDto tokenDto = jwtTokenService.reissueToken(request);
        return Api.OK(tokenDto);
    }
}
