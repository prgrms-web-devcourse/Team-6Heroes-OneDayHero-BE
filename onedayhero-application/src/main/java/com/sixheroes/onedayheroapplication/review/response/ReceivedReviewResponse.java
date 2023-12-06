package com.sixheroes.onedayheroapplication.review.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Builder
public record ReceivedReviewResponse(
        Long reviewId,

        Long senderId,

        String senderNickname,

        List<String> profileImage,

        String categoryName,

        String missionTitle,

        Integer starScore,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt
) {

        public static ReceivedReviewResponse from(
                ReceivedReviewQueryResponse queryResponse
        ) {
                return ReceivedReviewResponse.builder()
                        .reviewId(queryResponse.reviewId())
                        .senderId(queryResponse.senderId())
                        .senderNickname(queryResponse.senderNickname())
                        .profileImage(imageMapper(queryResponse.profileImage()))
                        .categoryName(queryResponse.categoryName())
                        .missionTitle(queryResponse.missionTitle())
                        .starScore(queryResponse.starScore())
                        .createdAt(queryResponse.createdAt())
                        .build();
        }

        public static List<String> imageMapper(String profileImage) {
                if (profileImage == null) {
                        return Collections.emptyList();
                }

                return List.of(profileImage);
        }
}
