package com.project.devtogether.member.dto;

import com.project.devtogether.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRegisterRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String name,
        String nickName,
        String introduce
) {

    public Member toEntity() {
        return Member.of(
                email,
                password,
                name,
                nickName,
                introduce
        );
    }
}
