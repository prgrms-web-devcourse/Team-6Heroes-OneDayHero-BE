package com.sixheroes.onedayheroapi.global.handler;

import com.sixheroes.onedayheroapi.global.response.ErrorResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
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
    public ErrorResponse handleException(Exception exception) {
        log.error("Error occurred", exception);
        var errorCode = ErrorCode.S_001;
        return ErrorResponse.from(errorCode);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        var errorCode = ErrorCode.findByName(exception.getMessage())
                .orElse(ErrorCode.S_001);

        if (errorCode == ErrorCode.S_001) {
            log.warn("Error occurred", exception);
            return ResponseEntity.internalServerError()
                    .body(ErrorResponse.from(errorCode));
        }

        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(errorCode));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public ErrorResponse handleBindException(BindException exception) {
        var errorCode = ErrorCode.T_001;
        return ErrorResponse.from(errorCode, exception);
    }
}
