package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserGender {
    MALE("남자"),
    FEMALE("여자"),
    OTHER("기타");

    private final String description;

    public static UserGender from(String userGender) {
        return Arrays.stream(UserGender.values())
                .filter(ug -> ug.isEqual(userGender))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INVALID_GENDER));
    }

    private boolean isEqual(String userGender) {
        return userGender.equals(this.name());
    }
}
