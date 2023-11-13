package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayherodomain.user.repository.dto.UserQueryDto;
import lombok.Builder;

@Builder
public record UserResponse(
    UserBasicInfoResponse basicInfo,
    UserImageResponse image,
    UserFavoriteWorkingDayResponse favoriteWorkingDay,
    Integer heroScore,
    Boolean isHeroMode
) {

    public static UserResponse from(
        UserQueryDto userQueryDto
    ) {
        var userBasicInfoResponse = UserBasicInfoResponse.from(userQueryDto);
        var userImageResponse = UserImageResponse.from(userQueryDto);
        var userFavoriteWorkingDayResponse = UserFavoriteWorkingDayResponse.from(userQueryDto);

        return UserResponse.builder()
            .basicInfo(userBasicInfoResponse)
            .image(userImageResponse)
            .favoriteWorkingDay(userFavoriteWorkingDayResponse)
            .heroScore(userQueryDto.heroScore())
            .isHeroMode(userQueryDto.isHeroMode())
            .build();
    }
}
