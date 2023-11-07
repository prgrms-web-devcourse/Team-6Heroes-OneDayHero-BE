package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record MissionProgressResponse(
        Long id,

        String title,

        MissionCategoryResponse missionCategory,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        Integer bookmarkCount,

        String missionStatus
) {

}
