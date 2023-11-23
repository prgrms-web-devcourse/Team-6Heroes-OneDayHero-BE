package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.Week;
import lombok.Builder;

import java.time.LocalTime;
import java.util.*;

@Builder
public record UserFavoriteWorkingDayResponse(
    List<String> favoriteDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteStartTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteEndTime
) {
    static final UserFavoriteWorkingDayResponse EMPTY = UserFavoriteWorkingDayResponse.builder()
        .favoriteDate(Collections.emptyList())
        .build();

    public static UserFavoriteWorkingDayResponse from(
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        if (Objects.isNull(userFavoriteWorkingDay)) {
            return EMPTY;
        }

        var favoriteDate = Optional.of(userFavoriteWorkingDay.getFavoriteDate())
            .orElseGet(ArrayList::new)
            .stream()
            .map(Week::name)
            .toList();

        return UserFavoriteWorkingDayResponse.builder()
            .favoriteDate(favoriteDate)
            .favoriteStartTime(userFavoriteWorkingDay.getFavoriteStartTime())
            .favoriteEndTime(userFavoriteWorkingDay.getFavoriteEndTime())
            .build();
    }
}
