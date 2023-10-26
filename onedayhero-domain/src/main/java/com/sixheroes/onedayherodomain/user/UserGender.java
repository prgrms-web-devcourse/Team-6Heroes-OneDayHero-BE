package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public enum UserGender {
    MALE("남자"),
    FEMALE("여자");

    private final String description;

    public static UserGender from(String userGender) {
        return Arrays.stream(UserGender.values())
                    .filter(ug -> userGender.equals(ug.name()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(ErrorCode.EU_007.name()));
    }
}