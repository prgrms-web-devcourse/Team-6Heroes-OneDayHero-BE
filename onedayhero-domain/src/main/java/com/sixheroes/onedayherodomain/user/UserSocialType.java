package com.sixheroes.onedayherodomain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSocialType {
    KAKAO("카카오");

    private final String description;
}