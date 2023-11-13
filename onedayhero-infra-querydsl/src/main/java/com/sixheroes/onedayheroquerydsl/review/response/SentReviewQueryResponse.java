package com.sixheroes.onedayheroquerydsl.review.response;

import java.time.LocalDateTime;

public record SentReviewQueryResponse(
        Long reviewId,
        String categoryName,
        String missionTitle,
        Integer starScore,
        LocalDateTime createdAt
) {
}
