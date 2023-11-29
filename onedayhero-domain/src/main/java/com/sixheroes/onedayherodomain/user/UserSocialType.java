package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.auth.InvalidAuthorizationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum UserSocialType {
    KAKAO("카카오");

    private final String description;

    public static UserSocialType findByName(String name) {
        return Arrays.stream(UserSocialType.values())
                .filter((userSocialType) -> userSocialType.name().equals(name))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 소셜 로그인 타입이 입력되었습니다.");
                    throw new InvalidAuthorizationException(ErrorCode.NOT_FOUNT_SOCIAL_TYPE);
                });
    }
}
