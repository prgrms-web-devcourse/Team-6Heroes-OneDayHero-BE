package com.sixheroes.onedayheroapplication.oauth.infra.feign.decoder;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class LoginErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        var feignException = FeignException.errorStatus(methodKey, response);
        var errorMessage = feignException.getMessage();
        log.warn("로그인 과정에서 에러가 발생했습니다. {}", errorMessage);

        return switch (response.status()) {
            case 400, 401, 500 -> new BusinessException(ErrorCode.LOGIN_FAIL);
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
