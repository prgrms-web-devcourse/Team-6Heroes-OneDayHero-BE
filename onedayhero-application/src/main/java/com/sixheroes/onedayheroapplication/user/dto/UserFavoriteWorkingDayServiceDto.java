package com.sixheroes.onedayheroapplication.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record UserFavoriteWorkingDayServiceDto(
    List<String> favoriteDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    LocalTime favoriteStartTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    LocalTime favoriteEndTime
) {
}
