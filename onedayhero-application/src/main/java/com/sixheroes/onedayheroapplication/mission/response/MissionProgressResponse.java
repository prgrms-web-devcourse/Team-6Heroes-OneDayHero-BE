package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionProgressQueryResponse;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MissionProgressResponse(
        Long id,

        String title,

        MissionCategoryResponse missionCategory,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate missionDate,

        Integer bookmarkCount,

        String missionStatus,

        String imagePath
) {

    public static MissionProgressResponse from(
            MissionProgressQueryResponse response,
            String imagePath
    ) {
        return MissionProgressResponse.builder()
                .id(response.id())
                .title(response.title())
                .missionCategory(MissionCategoryResponse.from(response))
                .missionDate(response.missionDate())
                .bookmarkCount(response.bookmarkCount())
                .missionStatus(response.missionStatus().name())
                .imagePath(imagePath)
                .build();
    }
}
