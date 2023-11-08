package com.sixheroes.onedayheroquerydsl.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionStatus;

import java.time.LocalDate;

public record MissionProgressQueryResponse(
        Long id,

        String title,

        Long categoryId,

        MissionCategoryCode categoryCode,

        String categoryName,

        String si,

        String gu,

        String dong,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        Integer bookmarkCount,

        MissionStatus missionStatus
) {
}
