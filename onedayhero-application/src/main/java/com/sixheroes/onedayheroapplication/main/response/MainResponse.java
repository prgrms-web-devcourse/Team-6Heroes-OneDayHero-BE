package com.sixheroes.onedayheroapplication.main.response;

import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record MainResponse(
        List<MissionCategoryResponse> missionCategories,
        List<MissionSoonExpiredResponse> soonExpiredMissions
) {

    public static MainResponse from(
            List<MissionCategoryResponse> missionCategories,
            List<MissionSoonExpiredResponse> soonExpiredMissions
    ) {
        return MainResponse.builder()
                .missionCategories(missionCategories)
                .soonExpiredMissions(soonExpiredMissions)
                .build();
    }
}
