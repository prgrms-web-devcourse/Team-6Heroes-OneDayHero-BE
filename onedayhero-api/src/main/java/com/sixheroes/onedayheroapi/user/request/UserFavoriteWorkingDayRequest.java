package com.sixheroes.onedayheroapi.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record UserFavoriteWorkingDayRequest(
    List<String> favoriteDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteStartTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteEndTime
) {
}
