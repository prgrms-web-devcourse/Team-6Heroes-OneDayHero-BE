package com.sixheroes.onedayherodomain.user;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public enum Week {
    MON("월요일"),
    TUE("화요일"),
    WED("수요일"),
    THU("목요일"),
    FRI("금요일"),
    SAT("토요일"),
    SUN("일요일");

    private final String description;

    public static Week from(String week) {
        return Arrays.stream(Week.values())
            .filter(w -> week.equals(w.name()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(ErrorCode.EU_008.name()));
    }
}