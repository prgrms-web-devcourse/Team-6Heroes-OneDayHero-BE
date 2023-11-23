package com.sixheroes.onedayherodomain.mission.repository.dto;

import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;

public record MissionCategoryDto(
    Long userId,
    MissionCategoryCode missionCategoryCode,
    String name
) {
}
