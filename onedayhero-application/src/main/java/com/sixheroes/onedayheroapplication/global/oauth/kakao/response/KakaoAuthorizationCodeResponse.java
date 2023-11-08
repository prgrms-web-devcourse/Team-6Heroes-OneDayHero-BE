package com.sixheroes.onedayheroapplication.global.oauth.kakao.response;

public record KakaoAuthorizationCodeResponse(
        String code,
        String error
) {
}
