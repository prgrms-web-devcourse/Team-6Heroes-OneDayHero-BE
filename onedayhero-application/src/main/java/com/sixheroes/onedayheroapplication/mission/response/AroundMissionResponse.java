package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AroundMissionResponse(
        Long id,

        MissionCategoryResponse missionCategory,

        RegionResponse region,

        String title,

        Double longitude,

        Double latitude,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime endTime,

        Integer price,

        String imagePath
) {
}
