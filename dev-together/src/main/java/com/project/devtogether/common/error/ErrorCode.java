package com.project.devtogether.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs{

    OK(200, 200, "성공"),
    BAD_REQUEST(400, 400, "잘못된 요청")
    ;

    private final Integer HttpStatusCode;
    private final Integer errorCode;
    private final String description;
}
