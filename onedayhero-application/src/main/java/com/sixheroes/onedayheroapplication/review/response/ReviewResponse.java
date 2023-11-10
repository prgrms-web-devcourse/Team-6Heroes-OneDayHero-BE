package com.sixheroes.onedayheroapplication.review.response;

import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ReviewResponse(
        Long id,

        Long senderId,

        Long receiverId,

        MissionCategoryResponse missionCategory,

        String missionTitle,

        String content,

        Integer starScore,

        List<ReviewImageResponse> reviewImageResponses
) {

    @Builder
    public record ReviewImageResponse(
            Long id,
            String path
    ) {
    }
}
