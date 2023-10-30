package com.sixheroes.onedayheroapplication.missionmatch.response;

import lombok.Builder;

@Builder
public record MissionMatchWithdrawResponse(
        Long id,
        Long citizenId,
        Long missionId
) {
}
