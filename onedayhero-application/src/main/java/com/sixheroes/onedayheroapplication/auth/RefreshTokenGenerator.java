package com.sixheroes.onedayheroapplication.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RefreshTokenGenerator {

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
