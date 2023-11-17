package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayheroapplication.user.RegionResponseMapper;
import com.sixheroes.onedayherodomain.region.Region;
import com.sixheroes.onedayherodomain.user.User;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ProfileHeroResponse(
    UserBasicInfoResponse basicInfo,
    UserImageResponse image,
    UserFavoriteWorkingDayResponse favoriteWorkingDay,
    Map<String, Map<String, List<RegionForUserResponse>>> favoriteRegions,
    Integer heroScore
) {

    public static ProfileHeroResponse from(
        User user,
        List<Region> regions
    ) {
        var userBasicInfoResponse = UserBasicInfoResponse.from(user.getUserBasicInfo());
        var userImageResponse = user.getUserImages().stream()
            .map(UserImageResponse::from)
            .findFirst()
            .orElseGet(UserImageResponse::empty);
        var userFavoriteWorkingDayResponse = UserFavoriteWorkingDayResponse.from(user.getUserFavoriteWorkingDay());
        var favoriteRegions = RegionResponseMapper.toFavoriteRegions(regions);

        return ProfileHeroResponse.builder()
            .basicInfo(userBasicInfoResponse)
            .image(userImageResponse)
            .favoriteWorkingDay(userFavoriteWorkingDayResponse)
            .favoriteRegions(favoriteRegions)
            .heroScore(user.getHeroScore())
            .build();
    }
}