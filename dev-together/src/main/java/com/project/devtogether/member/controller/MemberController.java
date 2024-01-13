package com.project.devtogether.member.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.member.dto.MemberLoginRequest;
import com.project.devtogether.member.dto.MemberRegisterRequest;
import com.project.devtogether.member.dto.MemberResponse;
import com.project.devtogether.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public Api<MemberResponse> register(
            @Valid
            @RequestBody Api<MemberRegisterRequest> request
    ) {
        MemberResponse response = memberService.register(request.getBody());
        return Api.OK(response);
    }

    @PostMapping("/login")
    public Api<TokenDto> login(
            @Valid
            @RequestBody Api<MemberLoginRequest> request
    ) {
        TokenDto token = memberService.login(request.getBody());
        return Api.OK(token);
    }
}
