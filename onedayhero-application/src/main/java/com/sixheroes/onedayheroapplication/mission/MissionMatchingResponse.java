package com.sixheroes.onedayheroapplication.mission;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionMatchingQueryResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MissionMatchingResponse(

    Long id,

    String title,

    MissionCategoryResponse missionCategory,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    LocalDate missionDate,

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
        return MissionMatchingResponse.builder()
            .id(response.id())
            .title(response.title())
            .missionCategory(MissionCategoryResponse.from(response))
            .missionDate(response.missionDate())
            .bookmarkCount(response.bookmarkCount())
            .missionStatus(response.missionStatus().name())
            .imagePath(imagePath)
            .isBookmarked(isBookmarked)
            .build();
    }
}
