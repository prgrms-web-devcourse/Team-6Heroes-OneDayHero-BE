package com.sixheroes.onedayheroapi.global.handler;

import com.sixheroes.onedayheroapi.global.response.ErrorResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handleException(Exception exception) {
        log.error("Error occurred", exception);
        var errorCode = ErrorCode.S_001;
        return ErrorResponse.from(errorCode);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException exception) {
        var errorCode = ErrorCode.valueOf(exception.getMessage());
        return ErrorResponse.from(errorCode);
    }

    @ExceptionHandler(value = BindException.class)
    public ErrorResponse handleBindException(BindException exception) {
        var errorCode = ErrorCode.T_001;
        return ErrorResponse.from(errorCode, exception);
    }
}
