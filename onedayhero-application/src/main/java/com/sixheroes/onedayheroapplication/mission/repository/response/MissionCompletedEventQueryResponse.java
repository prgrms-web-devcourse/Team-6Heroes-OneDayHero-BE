package com.sixheroes.onedayheroapplication.mission.repository.response;

import java.util.Map;

public record MissionCompletedEventQueryResponse(
    Long heroId,
    Long missionId,
    String missionTitle
) {

    public Map<String, Object> toMap() {
        return Map.ofEntries(
            Map.entry("missionId", this.missionId),
            Map.entry("missionTitle", this.missionTitle)
        );
    }
}
