package com.sixheroes.onedayheroapplication.missionmatch.response;

import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;
import lombok.Builder;

@Builder
public record MissionMatchCreateResponse(
        Long missionMatchId,
        Long missionId,
        Long heroId
) {

    public static MissionMatchCreateResponse from(MissionMatch missionMatch) {
        return MissionMatchCreateResponse.builder()
                .missionMatchId(missionMatch.getId())
                .missionId(missionMatch.getMissionId())
                .heroId(missionMatch.getHeroId())
                .build();
    }
}
