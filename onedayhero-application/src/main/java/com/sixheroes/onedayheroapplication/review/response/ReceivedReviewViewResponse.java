package com.sixheroes.onedayheroapplication.review.response;

import org.springframework.data.domain.Slice;

public record ReceivedReviewViewResponse(
        Long userId,
        Slice<ReceivedReviewResponse> receivedReviewResponses
) {
}
