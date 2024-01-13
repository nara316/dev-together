package com.project.devtogether.member.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.common.error.ErrorCode;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.member.dto.MemberLoginRequest;
import com.project.devtogether.member.dto.MemberRegisterRequest;
import com.project.devtogether.member.dto.MemberResponse;
import com.project.devtogether.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    public Api<MemberResponse> readMember(@PathVariable Long id) {
        MemberResponse result = memberService.readMember(id);
        return Api.OK(result);
    }

    @GetMapping("/me")
    public Api<MemberResponse> readMe() {
        MemberResponse result = memberService.readMe();
        return Api.OK(result);
    }
}
