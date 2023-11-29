package com.sixheroes.onedayherocommon.exception;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EntityNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
}
