package com.sixheroes.onedayheroapplication.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultNicknameGenerator {

    private static final String PREFIX = "user";

    public static String generate() {
        var uuid = UUID.randomUUID().toString();
        return "%s-%s-%s".formatted(PREFIX, uuid.substring(0, 5), uuid.substring(10, 15));
    }
}
