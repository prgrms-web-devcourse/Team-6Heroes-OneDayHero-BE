package com.sixheroes.onedayheroapplication.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.user.Week;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record UserFavoriteWorkingDayDto(
    List<Week> favoriteDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    LocalTime favoriteStartTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    LocalTime favoriteEndTime
) {
}
