package com.sixheroes.onedayheroapi.mission.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.request.MissionFindFilterServiceRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record MissionFindFilterRequest(
        @NotNull(message = "userId는 필수 값 입니다.")
        Long userId,

        List<String> missionCategoryCodes,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        List<LocalDate> missionDates,

        List<Long> regionIds
) {

    public MissionFindFilterServiceRequest toService() {
        return MissionFindFilterServiceRequest.builder()
                .missionCategoryCodes(missionCategoryCodes)
                .missionDates(missionDates)
                .regionIds(regionIds)
                .build();
    }
}
