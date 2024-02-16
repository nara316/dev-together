package com.project.devtogether.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum RedisErrorCode implements ErrorCodeIfs{

    CACHE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), 6501, "데이터를 캐시하는 것에 실패했습니다"),
    CACHE_NOT_EXIST(HttpStatus.INTERNAL_SERVER_ERROR.value(), 6502, "캐시에서 데이터가 존재하지 않습니다"),
    ;

    private final Integer HttpStatusCode;
    private final Integer errorCode;
    private final String description;
}
