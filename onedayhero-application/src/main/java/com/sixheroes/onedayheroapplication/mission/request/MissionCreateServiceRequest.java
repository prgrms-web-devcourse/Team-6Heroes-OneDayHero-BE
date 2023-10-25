package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import lombok.Builder;
import org.springframework.data.geo.Point;

@Builder
public record MissionCreateServiceRequest(
        Long missionCategoryId,
        Long citizenId,
        Long regionId,
        Double latitude,
        Double longitude,
        MissionInfoServiceRequest missionInfo
) {

    public Mission toEntity(MissionCategory missionCategory) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .citizenId(citizenId)
                .regionId(regionId)
                .location(new Point(latitude, longitude))
                .missionInfo(missionInfo.toVo())
                .build();
    }
}
