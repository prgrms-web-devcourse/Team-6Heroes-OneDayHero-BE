package com.sixheroes.onedayheroapplication.missionrequest.dto;

import lombok.Builder;

@Builder
public record MissionCategoryDto(
        String missionCategoryCode,
        String categoryName
) {
}
