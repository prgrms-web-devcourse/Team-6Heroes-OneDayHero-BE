package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserSocialType {
    KAKAO("카카오");

    private final String description;

    public static UserSocialType findByName(String name) {
        return Arrays.stream(UserSocialType.values())
                .filter((userSocialType) -> userSocialType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(ErrorCode.S_001.name()));
    }
}
