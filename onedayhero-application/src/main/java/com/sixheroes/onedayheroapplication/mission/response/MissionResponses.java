package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroquerydsl.mission.response.MissionQueryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public record MissionResponses(
        Slice<MissionResponse> missionResponses
) {

    public static MissionResponses from(
            Pageable pageable,
            Slice<MissionQueryResponse> response,
            boolean hasNext
    ) {
        var missionResponses = response.stream()
                .map(MissionResponse::from)
                .toList();

        var resultMissionResponses = new SliceImpl<>(missionResponses, pageable, hasNext);
        return new MissionResponses(resultMissionResponses);
    }
}
