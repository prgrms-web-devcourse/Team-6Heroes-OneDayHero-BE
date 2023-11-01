package com.sixheroes.onedayheroapplication.missionmatch.response;

import lombok.Builder;

@Builder
public record MissionMatchCancelResponse(
        Long id,
        Long citizenId,
        Long missionId
) {
}
