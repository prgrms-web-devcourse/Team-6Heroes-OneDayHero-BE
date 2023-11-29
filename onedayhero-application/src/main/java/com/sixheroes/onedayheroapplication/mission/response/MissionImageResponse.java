package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayherodomain.mission.MissionImage;
import lombok.Builder;

@Builder
public record MissionImageResponse(
        Long id,
        String path
) {

    public static MissionImageResponse from(
            Long id,
            String path
    ) {
        return MissionImageResponse.builder()
                .id(id)
                .path(path)
                .build();
    }

    public static MissionImageResponse from(
            MissionImage missionImage
    ) {
        return MissionImageResponse.builder()
                .id(missionImage.getId())
                .path(missionImage.getPath())
                .build();
    }
}
