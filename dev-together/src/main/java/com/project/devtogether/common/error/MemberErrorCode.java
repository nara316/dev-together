package com.project.devtogether.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
* member의 경우 1000번대 에러코드 사용
* */
@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeIfs{

    EMAIL_NOT_DUPLICATED(HttpStatus.BAD_REQUEST.value(), 1401, "이메일은 중복될 수 없습니다"),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 1402, "사용자를 찾을 수 없습니다")
    ;

    private final Integer HttpStatusCode;
    private final Integer errorCode;
    private final String description;
}
