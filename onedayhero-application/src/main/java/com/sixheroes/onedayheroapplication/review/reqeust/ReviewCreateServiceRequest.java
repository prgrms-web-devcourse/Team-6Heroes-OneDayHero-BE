package com.sixheroes.onedayheroapplication.review.reqeust;

import lombok.Builder;


@Builder
public record ReviewCreateServiceRequest(
        Long senderId,
        Long receiverId,
        Long missionCategoryId,
        String missionTitle,
        String content,
        Integer starScore
) {
}
