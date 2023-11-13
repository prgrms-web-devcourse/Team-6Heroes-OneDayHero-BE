package com.sixheroes.onedayheroapplication.missionmatch.response;

import lombok.Builder;

@Builder
public record MissionMatchCancelResponse(
        Long missionMatchId,
        Long citizenId,
        Long missionId
) {
}
