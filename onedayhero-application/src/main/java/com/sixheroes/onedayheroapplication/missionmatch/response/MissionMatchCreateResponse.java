package com.sixheroes.onedayheroapplication.missionmatch.response;

import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import lombok.Builder;

@Builder
public record MissionMatchCreateResponse(
        Long id,
        Long missionId,
        Long heroId
) {

    public static MissionMatchCreateResponse from(MissionMatch missionMatch) {
        return MissionMatchCreateResponse.builder()
                .id(missionMatch.getId())
                .missionId(missionMatch.getMissionId())
                .heroId(missionMatch.getHeroId())
                .build();
    }
}
