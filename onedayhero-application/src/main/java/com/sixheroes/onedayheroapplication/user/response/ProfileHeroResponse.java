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

    public static ProfileCitizenResponse from(
        User user
    ) {
        var userBasicInfoResponse = ProfileCitizenResponse.UserBasicInfoForProfileCitizenResponse.from(user.getUserBasicInfo());
        var userImageResponse = user.getUserImages().stream()
            .map(UserImageResponse::from)
            .findFirst()
            .orElseGet(UserImageResponse::empty);

        return ProfileCitizenResponse.builder()
            .basicInfo(userBasicInfoResponse)
            .image(userImageResponse)
            .heroScore(user.getHeroScore())
            .build();
    }
}