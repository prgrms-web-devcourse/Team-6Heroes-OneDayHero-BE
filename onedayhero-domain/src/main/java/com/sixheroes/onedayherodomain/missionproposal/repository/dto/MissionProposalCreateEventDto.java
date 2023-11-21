package com.sixheroes.onedayherodomain.missionproposal.repository.dto;

import java.util.Map;

public record MissionProposalCreateEventDto(
    Long heroId,
    String citizenNickname,
    Long missionId,
    String missionTitle
) {

    public Map<String, Object> toMap() {
        return Map.ofEntries(
            Map.entry("citizenNickname", this.citizenNickname),
            Map.entry("missionId", this.missionId),
            Map.entry("missionTitle", this.missionTitle)
        );
    }
}
