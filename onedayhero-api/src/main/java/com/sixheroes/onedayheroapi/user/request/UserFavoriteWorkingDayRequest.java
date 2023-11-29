package com.sixheroes.onedayheroapi.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record UserFavoriteWorkingDayRequest(
    @NotNull(message = "유저 선호 요일은 배열이어야 합니다.")
    List<String> favoriteDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteStartTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime favoriteEndTime
) {
}
