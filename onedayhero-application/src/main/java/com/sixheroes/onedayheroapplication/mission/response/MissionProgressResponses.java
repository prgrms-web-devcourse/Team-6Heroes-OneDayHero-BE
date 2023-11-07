package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroquerydsl.mission.response.MissionProgressQueryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public record MissionProgressResponses(
        Slice<MissionProgressResponse> missionProgressResponses
) {

    public static MissionProgressResponses from(
            Pageable pageable,
            Slice<MissionProgressQueryResponse> response,
            boolean hasNext
    ) {
        var missionProgressResponses = response.stream()
                .map(MissionProgressResponse::from)
                .toList();

        var missionProgressResponseSlice = new SliceImpl<>(missionProgressResponses, pageable, hasNext);
        return new MissionProgressResponses(missionProgressResponseSlice);
    }
}
