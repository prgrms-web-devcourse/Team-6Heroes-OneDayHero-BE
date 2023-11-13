package com.sixheroes.onedayheroapi.auth.response.oauth;

public record KakaoAuthorizationCodeResponse(
        String code,
        String error
) {
}
