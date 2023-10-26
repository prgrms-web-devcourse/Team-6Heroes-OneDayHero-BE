package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayherodomain.mission.MissionCategory;
import lombok.Builder;

@Builder
public record MissionCategoryResponse(
        Long categoryId,
        String code,
        String name
) {

    public MissionCategoryResponse(MissionCategory category) {
        this(category.getId(), category.getMissionCategoryCode().name(), category.getName());
    }
}
