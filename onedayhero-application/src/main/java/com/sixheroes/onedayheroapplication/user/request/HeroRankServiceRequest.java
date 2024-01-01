package com.sixheroes.onedayheroapplication.user.request;

import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record HeroRankServiceRequest(
    String regionName,
    MissionCategoryCode missionCategoryCode
) {

    public static HeroRankServiceRequest of(
        String regionName,
        String missionCategoryCode
    ) {
        return HeroRankServiceRequest.builder()
            .regionName(regionName)
            .missionCategoryCode(MissionCategoryCode.from(missionCategoryCode))
            .build();
    }
}
