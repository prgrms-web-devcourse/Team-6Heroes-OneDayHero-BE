package com.sixheroes.onedayheroapplication.oauth.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
public record LoginResponse(
        Long userId,

        String accessToken,

        @JsonIgnore
        String refreshToken,

        @JsonIgnore
        Boolean signUp
) {

        public static LoginResponse of(
                Long userId,
                Boolean sigunUp,
                String accessToken,
                String refreshToken
        ) {
                return LoginResponse.builder()
                        .userId(userId)
                        .signUp(sigunUp)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
        }
}
