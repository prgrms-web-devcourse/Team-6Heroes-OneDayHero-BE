package com.sixheroes.onedayheroapplication.mission.response;

import lombok.Builder;

@Builder
public record MissionBookmarkCancelResponse(
        Long id,
        Long missionId,
        Long userId
) {
}
