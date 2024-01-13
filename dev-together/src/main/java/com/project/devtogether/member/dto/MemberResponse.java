package com.project.devtogether.member.dto;

import com.project.devtogether.member.domain.Member;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String nickName,
        String introduce,
        String role,
        String status) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickName(),
                member.getIntroduce(),
                member.getRole().toString(),
                member.getStatus().toString()
        );
    }
}
