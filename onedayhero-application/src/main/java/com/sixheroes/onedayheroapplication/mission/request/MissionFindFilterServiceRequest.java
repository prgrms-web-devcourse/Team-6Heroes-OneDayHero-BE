package com.sixheroes.onedayheroapplication.mission.request;

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

}
