package com.sixheroes.onedayherocommon.exception.auth;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidAuthorizationException extends RuntimeException {

    private final ErrorCode errorCode;
}
