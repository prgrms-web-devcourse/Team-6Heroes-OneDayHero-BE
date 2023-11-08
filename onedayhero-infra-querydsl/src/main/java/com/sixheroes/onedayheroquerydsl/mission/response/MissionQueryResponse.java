package com.sixheroes.onedayheroquerydsl.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

public record MissionQueryResponse(
        Long id,

        Long categoryId,

        MissionCategoryCode categoryCode,

        String categoryName,

        Long citizenId,

        Long regionId,

        String si,

        String gu,

        String dong,

        Point location,

        String title,

        String content,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime endTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime deadlineTime,

        Integer price,

        Integer bookmarkCount,

        MissionStatus missionStatus
) {
}
