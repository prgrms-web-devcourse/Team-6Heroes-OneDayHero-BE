package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import lombok.Builder;

@Builder
public record MissionCategoryDto(
        String missionCategoryCode,
        String categoryName
) {
}
