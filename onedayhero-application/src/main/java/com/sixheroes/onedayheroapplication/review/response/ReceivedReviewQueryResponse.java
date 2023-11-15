package com.sixheroes.onedayheroapplication.review.response;

import java.time.LocalDateTime;

public record ReceivedReviewQueryResponse(
        Long reviewId,
        Long senderId,
        String categoryName,
        String missionTitle,
        Integer starScore,
        LocalDateTime createdAt
) {
}
