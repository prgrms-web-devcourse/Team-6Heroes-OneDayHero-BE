package com.sixheroes.onedayheroapplication.mission.response;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public record MissionResponses(
        Slice<MissionResponse> missionResponses
) {
    
    public static MissionResponses from(
            Pageable pageable,
            List<MissionResponse> missionResponses,
            boolean hasNext
    ) {
        var resultMissionResponses = new SliceImpl<>(missionResponses, pageable, hasNext);
        return new MissionResponses(resultMissionResponses);
    }
}
