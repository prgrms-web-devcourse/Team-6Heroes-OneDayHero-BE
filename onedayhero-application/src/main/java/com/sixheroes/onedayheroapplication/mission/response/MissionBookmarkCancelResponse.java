package com.sixheroes.onedayheroapplication.mission.response;

import lombok.Builder;

@Builder
public record MissionBookmarkCancelResponse(
        Long missionBookmarkId,
        Long missionId,
        Long userId
) {
}
