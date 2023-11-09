package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayheroquerydsl.mission.response.MissionProgressQueryResponse;
import lombok.Builder;
import org.springframework.data.domain.Slice;

@Builder
public record MissionProgressResponses(
        Slice<MissionProgressResponse> missionProgressResponses
) {

    public static MissionProgressResponses from(
            Slice<MissionProgressQueryResponse> response
    ) {
        var mappedMissionProgressResponse = response.map(MissionProgressResponse::from);
        return new MissionProgressResponses(mappedMissionProgressResponse);
    }
}
