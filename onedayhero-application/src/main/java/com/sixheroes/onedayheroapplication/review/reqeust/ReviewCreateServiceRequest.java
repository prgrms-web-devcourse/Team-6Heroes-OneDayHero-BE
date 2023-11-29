package com.sixheroes.onedayheroapplication.review.reqeust;

import com.sixheroes.onedayherodomain.review.Review;
import lombok.Builder;


@Builder
public record ReviewCreateServiceRequest(
        Long senderId,
        Long receiverId,
        Long missionId,
        Long categoryId,
        String missionTitle,
        String content,
        Integer starScore
) {

    public Review toEntity() {
        return Review.builder()
                .categoryId(categoryId)
                .missionTitle(missionTitle)
                .senderId(senderId)
                .receiverId(receiverId)
                .starScore(starScore)
                .content(content)
                .build();
    }
}
