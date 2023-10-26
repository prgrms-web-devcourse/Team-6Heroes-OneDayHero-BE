package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;

public record UserUpdateResponse(
    Long userId,
    UserBasicInfoDto userBasicInfo,
    UserFavoriteWorkingDayDto favoriteWorkingDay
) {

    public static UserUpdateResponse from(
        Long userId,
        UserBasicInfo userBasicInfo,
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        var userBasicInfoDto = new UserBasicInfoDto(
            userBasicInfo.getNickname(),
            userBasicInfo.getGender(),
            userBasicInfo.getBirth(),
            userBasicInfo.getIntroduce()
        );

        var userFavoriteWorkingDayDto = new UserFavoriteWorkingDayDto(
            userFavoriteWorkingDay.getFavoriteDate(),
            userFavoriteWorkingDay.getFavoriteStartTime(),
            userFavoriteWorkingDay.getFavoriteEndTime()
        );

        return new UserUpdateResponse(userId, userBasicInfoDto, userFavoriteWorkingDayDto);
    }
}
