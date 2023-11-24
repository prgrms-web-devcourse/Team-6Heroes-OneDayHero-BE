package com.sixheroes.onedayheroapi.mission.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.request.MissionFindFilterServiceRequest;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MissionFindFilterRequest(
        List<String> missionCategoryCodes,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        List<LocalDate> missionDates,

        List<Long> regionIds
) {

    public MissionFindFilterServiceRequest toService(
            Long userId
    ) {
        return MissionFindFilterServiceRequest.builder()
                .userId(userId)
                .missionCategoryCodes(missionCategoryCodes)
                .missionDates(missionDates)
                .regionIds(regionIds)
                .build();
    }
}
