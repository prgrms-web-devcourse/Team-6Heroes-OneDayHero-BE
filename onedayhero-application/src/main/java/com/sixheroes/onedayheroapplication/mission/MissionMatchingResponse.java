package com.sixheroes.onedayheroapplication.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionMatchingQueryResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record MissionMatchingResponse(

    Long id,

    String title,

    MissionCategoryResponse missionCategory,

    RegionResponse region,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime missionCreatedAt,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate missionDate,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime startTime,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    LocalTime endTime,

    Integer price,

    Integer bookmarkCount,

    String missionStatus,

    String imagePath,

    boolean isBookmarked
) {

    public static MissionMatchingResponse from(
        MissionMatchingQueryResponse response,
        String imagePath,
        boolean isBookmarked
    ) {
        var regionResponse = RegionResponse.from(response);

        return MissionMatchingResponse.builder()
            .id(response.id())
            .title(response.title())
            .missionCategory(MissionCategoryResponse.from(response))
            .region(regionResponse)
            .missionCreatedAt(response.missionCreatedAt())
            .missionDate(response.missionDate())
            .startTime(response.startTime())
            .endTime(response.endTime())
            .price(response.price())
            .bookmarkCount(response.bookmarkCount())
            .missionStatus(response.missionStatus().name())
            .imagePath(imagePath)
            .isBookmarked(isBookmarked)
            .build();
    }
}
