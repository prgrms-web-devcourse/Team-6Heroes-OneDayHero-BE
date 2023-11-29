package com.sixheroes.onedayheroapplication.missionmatch.event.dto;

import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;

public record MissionMatchRejectEvent(
    Long missionMatchId
) {

    public static MissionMatchRejectEvent from(
        MissionMatch missionMatch
    ) {
        return new MissionMatchRejectEvent(missionMatch.getId());
    }
}
