package com.project.devtogether.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    USER("일반회원"),
    ADMIN("관리자")
    ;

    private String description;
}
