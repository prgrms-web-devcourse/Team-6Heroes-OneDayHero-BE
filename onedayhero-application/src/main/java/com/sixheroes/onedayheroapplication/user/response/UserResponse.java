package com.sixheroes.onedayheroapplication.user.response;

public record UserResponse(
    UserBasicInfoResponse basicInfo,
    UserImageResponse image,
    UserFavoriteWorkingDayResponse favoriteWorkingDay,
    Integer heroScore,
    Boolean isHeroMode
) {
}
