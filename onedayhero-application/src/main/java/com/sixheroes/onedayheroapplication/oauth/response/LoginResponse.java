package com.sixheroes.onedayheroapplication.oauth.response;


public record LoginResponse(
        Long userId,
        String accessToken
) {
}
