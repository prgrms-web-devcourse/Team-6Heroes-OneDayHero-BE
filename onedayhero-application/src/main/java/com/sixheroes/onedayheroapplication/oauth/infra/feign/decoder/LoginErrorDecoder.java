package com.sixheroes.onedayheroapplication.oauth.infra.feign.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor
public class LoginErrorDecoder implements ErrorDecoder {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.warn("로그인 과정에서 에러가 발생했습니다.");

        return switch (response.status()) {
            case 400, 401, 500 -> new IllegalStateException(ErrorCode.L_001.name());
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
