package com.sixheroes.onedayheroapplication.auth;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Getter
public class RefreshToken {

    private static final String DEFAULT = "NONE";

    private Long userId;
    private String chainedToken;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(
            Long userId,
            String chainedToken
    ) {
        this.userId = userId;
        this.chainedToken = chainedToken;
    }

    public static RefreshToken issue(
            Long userId
    ) {
        return RefreshToken.builder()
                .userId(userId)
                .chainedToken(DEFAULT)
                .build();
    }

    public void setTokenChain(
            String refreshToken
    ) {
        this.chainedToken = refreshToken;
    }

    public Boolean validateChainable() {
        return Objects.equals(
                chainedToken,
                DEFAULT
        );
    }
}
