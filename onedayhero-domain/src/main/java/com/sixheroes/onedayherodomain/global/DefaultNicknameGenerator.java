package com.sixheroes.onedayherodomain.global;

import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public final class DefaultNicknameGenerator {

    private static final String PREFIX = "user";

    public static String generate() {
        var uuid = UUID.randomUUID().toString();
        return "%s-%s-%s".formatted(PREFIX, uuid.substring(0, 5), uuid.substring(10, 15));
    }
}
