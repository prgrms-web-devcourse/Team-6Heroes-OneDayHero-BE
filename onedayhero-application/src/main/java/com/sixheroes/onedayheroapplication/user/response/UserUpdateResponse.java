package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.Week;

public record UserUpdateResponse(
<<<<<<< HEAD
    Long id,
    UserBasicInfoServiceResponse basicInfo,
    UserFavoriteWorkingDayServiceResponse favoriteWorkingDay
=======
    Long userId,
    UserBasicInfoResponse basicInfo,
    UserFavoriteWorkingDayResponse favoriteWorkingDay
>>>>>>> c7b44e2... [SIX-162] feat & test : 자신의 프로필 조회 API 구현 및 RestDocs 작성
) {

    public static UserUpdateResponse of(
        Long userId,
        UserBasicInfo userBasicInfo,
        UserFavoriteWorkingDay userFavoriteWorkingDay
    ) {
        var userBasicInfoDto = new UserBasicInfoResponse(
            userBasicInfo.getNickname(),
            userBasicInfo.getGender().name(),
            userBasicInfo.getBirth(),
            userBasicInfo.getIntroduce()
        );

        var userFavoriteWorkingDayDto = new UserFavoriteWorkingDayResponse(
            userFavoriteWorkingDay.getFavoriteDate().stream()
                .map(Week::name)
                .toList(),
            userFavoriteWorkingDay.getFavoriteStartTime(),
            userFavoriteWorkingDay.getFavoriteEndTime()
        );

        return new UserUpdateResponse(userId, userBasicInfoDto, userFavoriteWorkingDayDto);
    }
}
