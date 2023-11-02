package com.sixheroes.onedayheroapplication.mission.response;

import org.springframework.data.domain.Slice;

public record MissionResponses(
        Slice<MissionResponse> missionResponses
) {

}
