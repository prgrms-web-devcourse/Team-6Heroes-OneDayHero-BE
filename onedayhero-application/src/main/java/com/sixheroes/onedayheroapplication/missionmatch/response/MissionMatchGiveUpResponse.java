package com.sixheroes.onedayheroapplication.missionmatch.response;

import lombok.Builder;

@Builder
public record MissionMatchGiveUpResponse(
        Long id,
        Long heroId,
        Long missionId
) {
}
