package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayherodomain.mission.MissionBookmark;
import lombok.Builder;


@Builder
public record MissionBookmarkCreateResponse(
        Long missionBookmarkId,
        Long missionId,
        Long userId
) {

    public MissionBookmarkCreateResponse(MissionBookmark missionBookmark) {
        this(
                missionBookmark.getId(),
                missionBookmark.getMission().getId(),
                missionBookmark.getUserId()
        );
    }
}
