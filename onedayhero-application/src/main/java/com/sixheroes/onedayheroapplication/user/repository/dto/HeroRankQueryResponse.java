package com.sixheroes.onedayheroapplication.user.repository.dto;

public record HeroRankQueryResponse(
    Long userId,
    String nickname,
    Integer heroScore,
    String profileImagePath
) {
}
