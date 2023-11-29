package com.sixheroes.onedayheroapplication.review.response;

import com.sixheroes.onedayherodomain.review.Review;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Builder
public record ReviewResponse(
        Long id
) {

    public static ReviewResponse from(
            Review review
    ) {
        return ReviewResponse.builder()
                .id(review.getId())
                .build();
    }
}
