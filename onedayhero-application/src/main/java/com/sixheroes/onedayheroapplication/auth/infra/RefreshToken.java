package com.sixheroes.onedayheroapplication.auth.infra;

import lombok.Builder;

@Builder
public record RefreshToken(
        Long userId,
        String chainedRefreshToken
) {

    private static final String DEFAULT = "NONE";

    public static RefreshToken issue(
            Long userId
    ) {
        return RefreshToken.builder()
                .userId(userId)
                .chainedRefreshToken(DEFAULT)
                .build();
    }
}
