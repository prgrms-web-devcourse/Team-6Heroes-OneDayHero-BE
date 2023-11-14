package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionBookmarkMeQueryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionBookmarkMeResponse(
        Long missionId,

        Long missionBookmarkId,

        Boolean isAlive,

        MissionBookmarkMeMissionInfoDto missionInfo,

        RegionResponse region
) {

    @Builder
    public record MissionBookmarkMeMissionInfoDto(
            String title,

            String categoryName,

            Integer bookmarkCount,

            Integer price,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate missionDate,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime startTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime endTime
    ) {
    }

    public static MissionBookmarkMeResponse from(
            MissionBookmarkMeQueryResponse queryResponse
    ) {
        return new MissionBookmarkMeResponse(
                queryResponse.missionId(),
                queryResponse.missionBookmarkId(),
                isAlive(queryResponse.missionStatus()),
                MissionBookmarkMeMissionInfoDto.builder()
                        .title(queryResponse.title())
                        .categoryName(queryResponse.categoryName())
                        .bookmarkCount(queryResponse.bookmarkCount())
                        .price(queryResponse.price())
                        .missionDate(queryResponse.missionDate())
                        .startTime(queryResponse.startTime())
                        .endTime(queryResponse.endTime())
                        .build(),
                RegionResponse.builder()
                        .id(queryResponse.regionId())
                        .si(queryResponse.si())
                        .gu(queryResponse.gu())
                        .dong(queryResponse.dong())
                        .build()
        );
    }

    private static Boolean isAlive(
            MissionStatus missionStatus
    ) {
        return missionStatus.isMatching();
    }
}
