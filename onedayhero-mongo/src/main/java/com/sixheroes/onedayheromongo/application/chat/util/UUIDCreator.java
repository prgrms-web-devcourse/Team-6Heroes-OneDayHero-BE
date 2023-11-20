package com.sixheroes.onedayheromongo.application.chat.util;

import java.util.UUID;

public final class UUIDCreator {

    private UUIDCreator() {

    }

    public static String createUUID() {
        return UUID.randomUUID()
                .toString();
    }
}
