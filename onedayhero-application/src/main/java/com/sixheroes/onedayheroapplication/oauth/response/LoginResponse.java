package com.sixheroes.onedayheroapplication.oauth.response;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record LoginResponse(
        @JsonIgnore
        Boolean signUp,
        Long userId,
        String accessToken
) {
}
