package com.sixheroes.onedayheroapplication.mission.response;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public record MissionProgressResponses(
        Slice<MissionProgressResponse> missionProgressResponses
) {
    public static MissionProgressResponses from(
            Pageable pageable,
            List<MissionProgressResponse> missionProgressResponses,
            boolean hasNext
    ) {
        var missionProgressResponseSlice = new SliceImpl<>(missionProgressResponses, pageable, hasNext);
        return new MissionProgressResponses(missionProgressResponseSlice);
    }
}
