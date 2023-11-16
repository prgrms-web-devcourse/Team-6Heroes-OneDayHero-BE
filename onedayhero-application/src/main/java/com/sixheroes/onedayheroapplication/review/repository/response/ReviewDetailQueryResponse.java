package com.sixheroes.onedayheroapplication.review.repository.response;


import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;

import java.time.LocalDateTime;

public record ReviewDetailQueryResponse(
        Long id,

        Long senderId,

        String senderNickname,

        Long receiverId,

        Long categoryId,

        MissionCategoryCode categoryCode,

        String categoryName,

        String missionTitle,

        String content,

        Integer starScore,

        LocalDateTime createdAt
) {
}
