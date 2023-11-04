package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayheroquerydsl.mission.response.MissionBookmarkMeQueryResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionBookmarkMeLineDto(
        Long missionId,

        Long missionBookmarkId,

        Boolean isAlive,

        String title,

        Integer bookmarkCount,

        Integer price,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime startTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        LocalTime endTime,

        String categoryName,

        String si,

        String gu,

        String dong
) {

    public static MissionBookmarkMeLineDto from(
            MissionBookmarkMeQueryResponse queryResponse
    ) {
        return new MissionBookmarkMeLineDto(
                queryResponse.missionId(),
                queryResponse.missionBookmarkId(),
                isAlive(queryResponse.missionStatus()),
                queryResponse.title(),
                queryResponse.bookmarkCount(),
                queryResponse.price(),
                queryResponse.missionDate(),
                queryResponse.startTime(),
                queryResponse.endTime(),
                queryResponse.categoryName(),
                queryResponse.si(),
                queryResponse.gu(),
                queryResponse.dong()
        );
    }

    private static Boolean isAlive(
            MissionStatus missionStatus
    ) {
        return missionStatus.isMatching();
    }
}
