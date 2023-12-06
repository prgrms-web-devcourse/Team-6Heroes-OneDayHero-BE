package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionCompletedQueryResponse;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MissionCompletedResponse(
        Long id,

        String title,

        MissionCategoryResponse missionCategory,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        Integer bookmarkCount,

        String missionStatus,

        String imagePath,

        String si,

        String gu,

        String dong,

        boolean isBookmarked
) {

    public static MissionCompletedResponse from(
            MissionCompletedQueryResponse response,
            String imagePath,
            boolean isBookmarked
    ) {
        return MissionCompletedResponse.builder()
                .id(response.id())
                .title(response.title())
                .missionCategory(MissionCategoryResponse.from(response))
                .missionDate(response.missionDate())
                .bookmarkCount(response.bookmarkCount())
                .missionStatus(response.missionStatus().name())
                .imagePath(imagePath)
                .si(response.si())
                .gu(response.gu())
                .dong(response.dong())
                .isBookmarked(isBookmarked)
                .build();
    }
}
