package com.sixheroes.onedayheroapplication.review.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewResponse(
        Long id,

        Long senderId,

        String senderNickname,

        Long receiverId,

        MissionCategoryResponse missionCategory,

        String missionTitle,

        String content,

        Integer starScore,

        List<ReviewImageResponse> reviewImageResponses,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {

    @Builder
    public record ReviewImageResponse(
            Long id,
            String uniqueName,
            String path
    ) {
    }
}
