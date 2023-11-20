package com.sixheroes.onedayheroapplication.review.response;

import java.time.LocalDateTime;

public record SentReviewQueryResponse(
        Long reviewId,
        String categoryName,
        String missionTitle,
        Integer starScore,
        String senderNickname,
        String profileImage,
        LocalDateTime createdAt
) {
}
