package com.sixheroes.onedayherodomain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ADMIN("운영자"),
    MEMBER("회원");

    private final String description;
}