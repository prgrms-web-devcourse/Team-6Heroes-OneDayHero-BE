package com.sixheroes.onedayheroapi.user.request;

import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserUpadateRequest(
    @NotNull(message = "유저 아이디가 null입니다.")
    Long userId,

    @Valid
    UserBasicInfoDto basicInfo,

    UserFavoriteWorkingDayDto favoriteWorkingDay
) {
    public UserServiceUpdateRequest toService() {
        return UserServiceUpdateRequest.from(
            userId,
            basicInfo.nickname(),
            basicInfo.gender(),
            basicInfo.birth(),
            basicInfo.introduce(),
            favoriteWorkingDay.favoriteDate(),
            favoriteWorkingDay.favoriteStartTime(),
            favoriteWorkingDay.favoriteEndTime()
        );
    }
}