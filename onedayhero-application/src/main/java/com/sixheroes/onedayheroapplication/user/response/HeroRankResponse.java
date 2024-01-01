package com.sixheroes.onedayheroapplication.user.response;

import com.sixheroes.onedayheroapplication.user.repository.dto.HeroRankQueryResponse;
import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder(access = AccessLevel.PRIVATE)
public record HeroRankResponse(
    Long id,
    String nickname,
    Integer heroScore,
    String profileImagePath,
    Integer rank,
    String missionCategory
) {

    public static HeroRankResponse of(
        HeroRankQueryResponse heroRankQueryResponse,
        Pageable pageable,
        int index
    ) {
        int rank = pageable.getPageNumber() * pageable.getPageSize() + index + 1;

        return HeroRankResponse.builder()
            .id(heroRankQueryResponse.userId())
            .nickname(heroRankQueryResponse.nickname())
            .heroScore(heroRankQueryResponse.heroScore())
            .profileImagePath(heroRankQueryResponse.profileImagePath())
            .rank(rank)
            .missionCategory(heroRankQueryResponse.missionCategoryName())
            .build();
    }
}
