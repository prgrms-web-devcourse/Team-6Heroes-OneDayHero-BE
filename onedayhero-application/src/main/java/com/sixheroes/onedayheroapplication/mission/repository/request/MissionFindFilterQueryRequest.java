package com.sixheroes.onedayheroapplication.mission.repository.request;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MissionFindFilterQueryRequest(
        Long userId,
        List<Long> missionCategoryIds,
        List<Long> regionIds,
        List<LocalDate> missionDates
) {
}
