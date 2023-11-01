package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayherodomain.mission.MissionCategory;
import lombok.Builder;

@Builder
public record MissionCategoryResponse(
        Long id,
        String code,
        String name
) {

    public static MissionCategoryResponse from(
            MissionCategory missionCategory
    ) {
        return MissionCategoryResponse.builder()
                .id(missionCategory.getId())
                .code(missionCategory.getMissionCategoryCode().name())
                .name(missionCategory.getName())
                .build();
    }
}
