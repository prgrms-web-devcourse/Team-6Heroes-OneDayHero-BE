package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.Week;
import com.sixheroes.onedayherodomain.user.repository.dto.UserQueryDto;

import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record UserFavoriteWorkingDayResponse(
    List<String> favoriteDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteStartTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteEndTime
) {

    public static UserFavoriteWorkingDayResponse from(
        UserQueryDto userQueryDto
    ) {
        var favoriteDate = userQueryDto.favoriteDate().stream()
            .map(Week::name)
            .toList();

        return UserFavoriteWorkingDayResponse.builder()
            .favoriteDate(favoriteDate)
            .favoriteStartTime(userQueryDto.favoriteStartTime())
            .favoriteEndTime(userQueryDto.favoriteEndTime())
            .build();
    }

    public static UserFavoriteWorkingDayResponse from(
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        var favoriteDate = userFavoriteWorkingDay.getFavoriteDate().stream()
            .map(Week::name)
            .toList();

        return UserFavoriteWorkingDayResponse.builder()
            .favoriteDate(favoriteDate)
            .favoriteStartTime(userFavoriteWorkingDay.getFavoriteStartTime())
            .favoriteEndTime(userFavoriteWorkingDay.getFavoriteEndTime())
            .build();
    }
}
