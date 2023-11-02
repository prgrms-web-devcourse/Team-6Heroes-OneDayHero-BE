package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayheroquerydsl.mission.response.MissionQueryResponse;
import lombok.Builder;

@Builder
public record MissionCategoryResponse(
        Long id,
        String code,
        String name
) {
    public static MissionCategoryResponse from(
            MissionQueryResponse response
    ) {
        return MissionCategoryResponse.builder()
                .id(response.categoryId())
                .code(response.categoryCode().name())
                .name(response.categoryName())
                .build();
    }

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
