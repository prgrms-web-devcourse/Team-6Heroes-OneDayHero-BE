package com.sixheroes.onedayheroapplication.review.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SentReviewResponse(
        Long reviewId,

        String categoryName,

        String missionTitle,

        Integer starScore,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {

        public static SentReviewResponse from(
                SentReviewQueryResponse queryResponse
        ) {
                return SentReviewResponse.builder()
                        .reviewId(queryResponse.reviewId())
                        .categoryName(queryResponse.categoryName())
                        .missionTitle(queryResponse.missionTitle())
                        .starScore(queryResponse.starScore())
                        .createdAt(queryResponse.createdAt())
                        .build();
        }
}
