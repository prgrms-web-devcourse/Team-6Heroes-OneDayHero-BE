package com.sixheroes.onedayheromongo.chat.util;

import java.util.UUID;

public final class UUIDCreator {

    private UUIDCreator() {

    }

    public static String createUUID() {
        return UUID.randomUUID()
                .toString();
    }
}
