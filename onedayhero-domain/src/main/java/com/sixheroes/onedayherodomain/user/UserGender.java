package com.sixheroes.onedayherodomain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGender {
    MALE("남자"),
    FEMALE("여자");

    private final String description;
}