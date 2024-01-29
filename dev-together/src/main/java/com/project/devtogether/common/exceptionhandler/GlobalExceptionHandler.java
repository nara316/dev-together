package com.project.devtogether.common.exceptionhandler;

import com.project.devtogether.common.api.Api;
import com.project.devtogether.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api<Object>> methodException(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(400)
                .body(
                        Api.ERROR(ErrorCode.BAD_REQUEST,
                                exception.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                );
    }
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception (
            Exception exception
    ){
        log.error("",exception);

        return ResponseEntity
                .status(500)
                .body(
                        Api.ERROR(ErrorCode.SERVER_ERROR)
                );
    }
}
