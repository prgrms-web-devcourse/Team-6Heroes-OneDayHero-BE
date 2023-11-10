package com.sixheroes.onedayheroapplication.user.response;

public record ProfileHeroResponse(
    UserBasicInfoResponse basicInfo,
    UserImageResponse image,
    UserFavoriteWorkingDayResponse favoriteWorkingDay,
    Integer heroScore
) {
}
