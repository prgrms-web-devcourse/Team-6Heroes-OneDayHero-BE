package com.sixheroes.onedayheroapplication.user.repository.dto;

public record HeroRankQueryResponse(
    String missionCategoryName,
    Long userId,
    String nickname,
    Integer heroScore,
    String profileImagePath
) {
}
