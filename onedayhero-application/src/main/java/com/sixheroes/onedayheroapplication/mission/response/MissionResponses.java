package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroapplication.mission.repository.response.MissionQueryResponse;
import org.springframework.data.domain.Slice;

public record MissionResponses(
        Slice<MissionResponse> missionResponses
) {

    public static MissionResponses from(
            Slice<MissionQueryResponse> response
    ) {
        var mappedMissionResponse = response.map(MissionResponse::from);
        return new MissionResponses(mappedMissionResponse);
    }
}
