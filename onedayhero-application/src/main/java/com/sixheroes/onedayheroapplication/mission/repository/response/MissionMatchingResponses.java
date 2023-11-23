package com.sixheroes.onedayheroapplication.mission.repository.response;

import com.sixheroes.onedayheroapplication.mission.MissionMatchingResponse;

import java.util.List;

public record MissionMatchingResponses(
    List<MissionMatchingResponse> missionMatchingResponses
) {
}
