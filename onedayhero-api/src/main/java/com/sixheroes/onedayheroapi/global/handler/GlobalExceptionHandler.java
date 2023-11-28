package com.sixheroes.onedayheroapi.global.handler;

import com.sixheroes.onedayheroapi.global.response.ErrorResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.auth.ExpiredTokenException;
import com.sixheroes.onedayherocommon.exception.auth.InvalidAuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handleException(
            Exception exception
    ) {
        log.error("Error occurred", exception);
        var errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ErrorResponse.from(errorCode);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException exception
    ) {
        log.warn("예외 처리가 되지 않은 Error 발생", exception);
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public ErrorResponse handleBindException(
            BindException exception
    ) {
        var errorCode = ErrorCode.INVALID_REQUEST_VALUE;
        return ErrorResponse.from(errorCode, exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ExpiredTokenException.class)
    public ErrorResponse handleTokenException(
            ExpiredTokenException exception
    ) {
        log.warn("만료된 토큰으로 접근하였습니다.", exception);

        var errorCode = ErrorCode.EXPIRED_TOKEN;
        return ErrorResponse.from(errorCode);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidAuthorizationException.class)
    public ErrorResponse handleTokenException(
            InvalidAuthorizationException exception
    ) {
        log.warn("유효하지 않은 토큰으로 접근하였습니다.", exception);

        var errorCode = ErrorCode.UNAUTHORIZED_TOKEN_REQUEST;
        return ErrorResponse.from(errorCode);
    }
}
