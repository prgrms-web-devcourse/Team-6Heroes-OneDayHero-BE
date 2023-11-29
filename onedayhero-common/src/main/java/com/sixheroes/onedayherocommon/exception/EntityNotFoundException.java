package com.sixheroes.onedayherocommon.exception;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
