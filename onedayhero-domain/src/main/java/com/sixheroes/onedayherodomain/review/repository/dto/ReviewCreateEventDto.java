package com.sixheroes.onedayherodomain.review.repository.dto;

import java.util.Map;

public record ReviewCreateEventDto(
    Long receiverId,
    String senderNickname,
    Long reviewId,
    String missionTitle
) {

    public Map<String, Object> toMap() {
        return Map.ofEntries(
            Map.entry("senderNickname", this.senderNickname),
            Map.entry("reviewId", this.reviewId),
            Map.entry("missionTitle", this.missionTitle)
        );
    }
}
