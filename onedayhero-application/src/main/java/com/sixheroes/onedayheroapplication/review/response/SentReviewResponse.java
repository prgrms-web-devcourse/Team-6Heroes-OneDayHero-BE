package com.sixheroes.onedayheroapplication.review.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
public record SentReviewResponse(
        Long reviewId,

        String categoryName,

        String missionTitle,

        Integer starScore,

        String senderNickname,

        List<String> profileImage,

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
                        .senderNickname(queryResponse.senderNickname())
                        .profileImage(imageMapper(queryResponse.profileImage()))
                        .createdAt(queryResponse.createdAt())
                        .build();
        }

        private static List<String> imageMapper(String profileImage) {
                if (profileImage == null) {
                        return Collections.emptyList();
                }

                return List.of(profileImage);
        }
}
