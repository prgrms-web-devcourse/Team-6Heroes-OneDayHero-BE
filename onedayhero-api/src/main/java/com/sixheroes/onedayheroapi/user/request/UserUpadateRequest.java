package com.sixheroes.onedayheroapi.user.request;

import com.sixheroes.onedayheroapplication.user.request.UserBasicInfoServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserFavoriteWorkingDayServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserUpadateRequest(
        @NotNull(message = "유저 아이디는 필수값 입니다.")
        Long userId,

        @Valid
        UserBasicInfoDto basicInfo,

        UserFavoriteWorkingDayDto favoriteWorkingDay
) {

    public UserServiceUpdateRequest toService() {
        var userBasicInfoServiceDto = UserBasicInfoServiceRequest.builder()
            .nickname(basicInfo.nickname())
            .gender(basicInfo.gender())
            .birth(basicInfo.birth())
            .introduce(basicInfo.introduce())
            .build();

        var userFavoriteWorkingDayServiceDto = UserFavoriteWorkingDayServiceRequest.builder()
            .favoriteDate(favoriteWorkingDay.favoriteDate())
            .favoriteStartTime(favoriteWorkingDay.favoriteStartTime())
            .favoriteEndTime(favoriteWorkingDay.favoriteEndTime())
            .build();

        return UserServiceUpdateRequest.builder()
                .userId(userId)
                .userBasicInfo(userBasicInfoServiceDto)
                .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
                .build();
    }
}