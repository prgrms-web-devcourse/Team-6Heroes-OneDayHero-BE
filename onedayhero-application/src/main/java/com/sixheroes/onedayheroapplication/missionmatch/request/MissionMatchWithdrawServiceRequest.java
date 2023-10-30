package com.sixheroes.onedayheroapplication.missionmatch.request;

import lombok.Builder;

@Builder
public record MissionMatchWithdrawServiceRequest(
        Long citizenId,
        Long missionId
) {
}
