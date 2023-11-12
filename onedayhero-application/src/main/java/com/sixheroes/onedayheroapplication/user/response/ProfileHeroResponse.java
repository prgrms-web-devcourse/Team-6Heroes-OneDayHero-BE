package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.User;
import lombok.Builder;

@Builder
public record ProfileHeroResponse(
    UserBasicInfoResponse basicInfo,
    UserImageResponse image,
    UserFavoriteWorkingDayResponse favoriteWorkingDay,
    Integer heroScore
) {

    public static ProfileHeroResponse from(
        User user
    ) {
        var userBasicInfoResponse = UserBasicInfoResponse.from(user.getUserBasicInfo());
        var userImageResponse = user.getUserImages().stream()
            .map(UserImageResponse::from)
            .findFirst()
            .orElseGet(UserImageResponse::empty);
        var userFavoriteWorkingDayResponse = UserFavoriteWorkingDayResponse.from(user.getUserFavoriteWorkingDay());

        return ProfileHeroResponse.builder()
            .basicInfo(userBasicInfoResponse)
            .image(userImageResponse)
            .favoriteWorkingDay(userFavoriteWorkingDayResponse)
            .heroScore(user.getHeroScore())
            .build();
    }
}