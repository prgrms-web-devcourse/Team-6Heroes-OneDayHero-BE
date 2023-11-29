package com.sixheroes.onedayheroapplication.main.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherodomain.mission.repository.response.MainMissionQueryResponse;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MissionSoonExpiredResponse(
        Long id,

        String title,

        RegionResponse region,

        MissionCategoryResponse missionCategory,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        Integer bookmarkCount,

        String missionStatus,

        String imagePath,

        boolean isBookmarked
) {

    public static MissionSoonExpiredResponse from(
            MainMissionQueryResponse response,
            String imagePath
    ) {
        return MissionSoonExpiredResponse.builder()
                .id(response.getId())
                .title(response.getTitle())
                .missionCategory(MissionCategoryResponse.builder()
                        .id(response.getCategoryId())
                        .code(response.getCategoryCode())
                        .name(response.getCategoryName())
                        .build())
                .missionDate(response.getMissionDate())
                .bookmarkCount(response.getBookmarkCount())
                .missionStatus(response.getMissionStatus().name())
                .region(RegionResponse.builder()
                        .id(response.getRegionId())
                        .si(response.getSi())
                        .gu(response.getGu())
                        .dong(response.getDong())
                        .build())
                .imagePath(imagePath)
                .isBookmarked(isBookmarked(response.getBookmarkId()))
                .build();
    }

    private static boolean isBookmarked(
            Long bookmarkId
    ) {
        return bookmarkId != null;
    }
}
