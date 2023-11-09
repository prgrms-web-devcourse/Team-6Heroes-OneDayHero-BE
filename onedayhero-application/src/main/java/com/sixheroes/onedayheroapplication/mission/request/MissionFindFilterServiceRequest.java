package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayheroquerydsl.mission.request.MissionFindFilterQueryRequest;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MissionFindFilterServiceRequest(
        Long userId,
        List<String> missionCategoryCodes,
        List<LocalDate> missionDates,
        List<Long> regionIds
) {

    public MissionFindFilterQueryRequest toQuery() {
        return MissionFindFilterQueryRequest.builder()
                .userId(userId)
                .missionCategoryIds(convertMissionCategoryCodesToLong(missionCategoryCodes))
                .missionDates(missionDates)
                .regionIds(regionIds)
                .build();
    }

    private List<Long> convertMissionCategoryCodesToLong(List<String> missionCategoryCodes) {
        return missionCategoryCodes.stream()
                .map(MissionCategoryCode::valueOf)
                .map(MissionCategoryCode::getCategoryId)
                .toList();
    }
}
