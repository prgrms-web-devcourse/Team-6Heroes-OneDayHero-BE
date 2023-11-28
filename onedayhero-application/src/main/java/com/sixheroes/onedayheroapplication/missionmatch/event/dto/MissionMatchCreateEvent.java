package com.sixheroes.onedayheroapplication.missionmatch.event.dto;

import com.sixheroes.onedayherodomain.missionmatch.MissionMatch;

public record MissionMatchCreateEvent(
    Long missionMatchId
) {

    public static MissionMatchCreateEvent from(
        MissionMatch missionMatch
    ) {
        return new MissionMatchCreateEvent(missionMatch.getId());
    }
}
