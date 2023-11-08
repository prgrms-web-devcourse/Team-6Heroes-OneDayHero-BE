package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.Week;

public record UserUpdateResponse(
    Long id,
    UserBasicInfoServiceResponse basicInfo,
    UserFavoriteWorkingDayServiceResponse favoriteWorkingDay
) {

    public static UserUpdateResponse of(
        Long userId,
        UserBasicInfo userBasicInfo,
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        var userBasicInfoDto = new UserBasicInfoServiceResponse(
            userBasicInfo.getNickname(),
            userBasicInfo.getGender().name(),
            userBasicInfo.getBirth(),
            userBasicInfo.getIntroduce()
        );

        var userFavoriteWorkingDayDto = new UserFavoriteWorkingDayServiceResponse(
            userFavoriteWorkingDay.getFavoriteDate().stream()
                .map(Week::name)
                .toList(),
            userFavoriteWorkingDay.getFavoriteStartTime(),
            userFavoriteWorkingDay.getFavoriteEndTime()
        );

        return new UserUpdateResponse(userId, userBasicInfoDto, userFavoriteWorkingDayDto);
    }
}
