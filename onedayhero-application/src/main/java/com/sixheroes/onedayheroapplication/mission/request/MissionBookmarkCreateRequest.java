package com.sixheroes.onedayheroapplication.mission.request;

import lombok.Builder;

@Builder
public record MissionBookmarkCreateRequest(
        Long missionId,
        Long userId
) {
}
