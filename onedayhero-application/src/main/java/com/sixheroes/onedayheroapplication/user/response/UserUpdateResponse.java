package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.User;
import lombok.Builder;

@Builder
public record UserUpdateResponse(
    Long id,
    UserBasicInfoResponse basicInfo,
    UserFavoriteWorkingDayResponse favoriteWorkingDay
) {

    public static UserUpdateResponse from(
        User user
    ) {
        var userBasicInfoResponse = UserBasicInfoResponse.from(user.getUserBasicInfo());
        var userFavoriteWorkingDayResponse = UserFavoriteWorkingDayResponse.from(user.getUserFavoriteWorkingDay());

        return UserUpdateResponse.builder()
            .id(user.getId())
            .basicInfo(userBasicInfoResponse)
            .favoriteWorkingDay(userFavoriteWorkingDayResponse)
            .build();
    }
}
