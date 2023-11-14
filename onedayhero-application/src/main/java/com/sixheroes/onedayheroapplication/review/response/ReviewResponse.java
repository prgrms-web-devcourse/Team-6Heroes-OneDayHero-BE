package com.sixheroes.onedayheroapplication.review.response;

import com.sixheroes.onedayherodomain.review.Review;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Builder
public record ReviewResponse(
        Long id,
        Long senderId,
        Long receiverId,
        Long categoryId,
        String missionTitle,
        Integer starScore,
        String content,
        List<ReviewImageResponse> reviewImageResponses
) {

    public static ReviewResponse from(
            Review review
    ) {
        return ReviewResponse.builder()
                .id(review.getId())
                .categoryId(review.getCategoryId())
                .missionTitle(review.getMissionTitle())
                .senderId(review.getSenderId())
                .receiverId(review.getReceiverId())
                .starScore(review.getStarScore())
                .content(review.getContent())
                .reviewImageResponses(reviewImageResponseMapper.apply(review))
                .build();
    }

    private static Function<Review, List<ReviewImageResponse>> reviewImageResponseMapper = review -> {
        if (review.hasImage()) {
            return review.getReviewImages()
                    .stream()
                    .map(ReviewImageResponse::from)
                    .toList();
        }

        return Collections.emptyList();
    };
}
