package com.project.devtogether.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
* member 관련 1000번대 에러코드 사용
* token 관련 5000번대 에러코드 사용
* */
@AllArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCodeIfs{

    EMAIL_NOT_DUPLICATED(HttpStatus.BAD_REQUEST.value(), 1401, "이메일은 중복될 수 없습니다"),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 1402, "사용자를 찾을 수 없습니다"),
    TOKEN_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR.value(), 5501, "토큰이 존재하지 않습니다"),
    TOKEN_IS_EXPIRED(HttpStatus.INTERNAL_SERVER_ERROR.value(), 5502, "토큰이 만료되었습니다"),
    TOKEN_IS_NOT_SAME(HttpStatus.INTERNAL_SERVER_ERROR.value(), 5503, "토큰이 일치하지 않습니다"),
    ;

    private final Integer HttpStatusCode;
    private final Integer errorCode;
    private final String description;
}
