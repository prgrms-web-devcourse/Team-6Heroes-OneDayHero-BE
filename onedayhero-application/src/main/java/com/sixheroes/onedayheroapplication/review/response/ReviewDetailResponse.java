package com.sixheroes.onedayheroapplication.review.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewDetailResponse(
        Long id,

        String nickname,

        MissionCategoryResponse missionCategory,

        String missionTitle,

        Integer starScore,

        String content,

        List<ReviewResponse.ReviewImageResponse> reviewImageResponses,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {
}
