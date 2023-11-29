package com.sixheroes.onedayheroapi.auth.response.oauth;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "인증 코드는 필수 값 입니다.")
        String code
) {
}
