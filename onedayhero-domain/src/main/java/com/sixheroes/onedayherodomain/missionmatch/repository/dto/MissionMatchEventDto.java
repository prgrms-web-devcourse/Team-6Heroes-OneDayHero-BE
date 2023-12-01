package com.sixheroes.onedayherodomain.missionmatch.repository.dto;

import java.util.Map;

public record MissionMatchEventDto(
    Long receiverId,
    String senderNickname,
    Long missionId,
    String missionTitle
) {

    public Map<String, Object> toMap() {
        return Map.ofEntries(
            Map.entry("senderNickname", this.senderNickname),
            Map.entry("missionId", this.missionId),
            Map.entry("missionTitle", this.missionTitle)
        );
    }
}
