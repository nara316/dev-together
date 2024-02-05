package com.project.devtogether.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest (
        @NotBlank String nickName,
        String introduce
){
}
