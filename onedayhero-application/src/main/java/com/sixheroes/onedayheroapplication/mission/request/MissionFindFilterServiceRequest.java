package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayheroapplication.mission.repository.request.MissionFindFilterQueryRequest;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Collections;
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

    private List<Long> convertMissionCategoryCodesToLong(
            List<String> missionCategoryCodes
    ) {
        if (missionCategoryCodes == null || missionCategoryCodes.isEmpty()) {
            return Collections.emptyList();
        }

        return missionCategoryCodes.stream()
                .map(MissionCategoryCode::valueOf)
                .map(MissionCategoryCode::getCategoryId)
                .toList();
    }
}
