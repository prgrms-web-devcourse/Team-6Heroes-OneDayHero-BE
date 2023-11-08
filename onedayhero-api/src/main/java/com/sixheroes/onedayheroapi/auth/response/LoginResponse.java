package com.sixheroes.onedayheroapi.auth.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        Long userId,
        String accessToken
) {
}
