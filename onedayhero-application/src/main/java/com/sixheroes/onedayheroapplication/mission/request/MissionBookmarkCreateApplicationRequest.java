package com.sixheroes.onedayheroapplication.mission.request;

import lombok.Builder;

@Builder
public record MissionBookmarkCreateApplicationRequest(
        Long missionId,
        Long userId

) {
}
