package com.sixheroes.onedayheroapplication.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RefreshTokenSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //Convert RefreshToken object to String of JsonType
    public static String serialize(
            RefreshToken refreshToken
    ) {
        try {
            return objectMapper.writeValueAsString(refreshToken);
        } catch (IOException exception) {
            throw new IllegalStateException();
        }
    }

    //Convert String of JsonType to RefreshToken object
    public static RefreshToken deserialize(
            String jsonTypeRefreshToken
    ) {
        try {
            return objectMapper.readValue(jsonTypeRefreshToken, RefreshToken.class);
        } catch (IOException exception) {
            throw new IllegalStateException();
        }
    }
}
