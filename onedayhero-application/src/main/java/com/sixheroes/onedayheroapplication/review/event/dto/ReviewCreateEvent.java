package com.sixheroes.onedayheroapplication.review.event.dto;

import com.sixheroes.onedayherodomain.review.Review;

public record ReviewCreateEvent(
    Long reviewId
) {

    public static ReviewCreateEvent from(
        Review review
    ) {
        return new ReviewCreateEvent(review.getId());
    }
}
