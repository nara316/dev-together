package com.project.devtogether.member.controller;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.common.token.dto.TokenDto;
import com.project.devtogether.member.dto.MemberLoginRequest;
import com.project.devtogether.member.dto.MemberRegisterRequest;
import com.project.devtogether.member.dto.MemberResponse;
import com.project.devtogether.member.dto.MemberUpdateRequest;
import com.project.devtogether.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "회원가입 API")
    @PostMapping("/register")
    public Api<MemberResponse> register(
            @Valid @RequestBody MemberRegisterRequest request
    ) {
        MemberResponse response = memberService.register(request);
        return Api.OK(response);
    }

    @Operation(summary = "로그인 API", description = "ID와 PWD를 확인 후 TOKEN을 발행한다.")
    @PostMapping("/login")
    public Api<TokenDto> login(
            @Valid @RequestBody MemberLoginRequest request
    ) {
        TokenDto token = memberService.login(request);
        return Api.OK(token);
    }

    @Operation(summary = "회원정보 조회 API", description = "해당 회원ID의 회원정보를 조회한다.")
    @GetMapping("{id}")
    public Api<MemberResponse> readMember(@PathVariable("id") Long id) {
        MemberResponse result = memberService.readMember(id);
        return Api.OK(result);
    }

    @Operation(summary = "회원정보 조회 API", description = "로그인한 본인의 회원정보를 조회한다.")
    @GetMapping("/me")
    public Api<MemberResponse> readMe() {
        MemberResponse result = memberService.readMe();
        return Api.OK(result);
    }

    @Operation(summary = "회원정보 수정 API", description = "로그인한 본인의 회원정보를 수정한다.")
    @PatchMapping("/me")
    public Api<MemberResponse> updateMe(@Valid @RequestBody MemberUpdateRequest request) {
        MemberResponse result = memberService.updateMe(request);
        return Api.OK(result);
    }

    @Operation(summary = "로그아웃 API")
    @PatchMapping("/logout")
    public Api<String> logout(HttpServletRequest request) {
        memberService.logout(request);
        return Api.OK("로그아웃이 성공적으로 완료되었습니다.");
    }
}
