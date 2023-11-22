package com.sixheroes.onedayheroapplication.main;

import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionSoonExpiredResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record MainResponse(
        RegionResponse region,
        List<MissionCategoryResponse> missionCategories,
        List<MissionSoonExpiredResponse> soonExpiredMissions
) {

}
