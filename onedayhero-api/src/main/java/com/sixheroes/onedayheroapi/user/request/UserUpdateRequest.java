package com.sixheroes.onedayheroapi.user.request;

import com.sixheroes.onedayheroapplication.user.request.UserBasicInfoServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserFavoriteWorkingDayServiceRequest;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public record UserUpdateRequest(
        @Valid
        UserBasicInfoRequest basicInfo,

        UserFavoriteWorkingDayRequest favoriteWorkingDay,

        List<Long> favoriteRegions
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
                .userBasicInfo(userBasicInfoServiceDto)
                .userFavoriteWorkingDay(userFavoriteWorkingDayServiceDto)
                .userFavoriteRegions(favoriteRegions)
                .build();
    }
}