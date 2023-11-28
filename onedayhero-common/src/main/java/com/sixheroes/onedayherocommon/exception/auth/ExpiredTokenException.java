package com.sixheroes.onedayherocommon.exception.auth;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExpiredTokenException extends RuntimeException {

    private final ErrorCode errorCode;
}
