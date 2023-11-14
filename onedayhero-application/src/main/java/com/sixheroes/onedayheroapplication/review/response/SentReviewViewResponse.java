package com.sixheroes.onedayheroapplication.review.response;

import org.springframework.data.domain.Slice;

public record SentReviewViewResponse(
        Long userId,
        Slice<SentReviewResponse> sentReviewResponses
) {
}
