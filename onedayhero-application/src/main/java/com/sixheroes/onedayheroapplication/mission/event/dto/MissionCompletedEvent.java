package com.sixheroes.onedayheroapplication.mission.event.dto;

import com.sixheroes.onedayherodomain.mission.Mission;

public record MissionCompletedEvent(
    Long missionId
) {

    public static MissionCompletedEvent from(
        Mission mission
    ) {
        return new MissionCompletedEvent(mission.getId());
    }
}
