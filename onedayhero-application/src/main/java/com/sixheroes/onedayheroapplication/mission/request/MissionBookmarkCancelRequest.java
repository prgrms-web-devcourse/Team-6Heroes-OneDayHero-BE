package com.sixheroes.onedayheroapplication.mission.request;

import lombok.Builder;

@Builder
public record MissionBookmarkCancelRequest(
        Long missionId,
        Long userId
) {
}
