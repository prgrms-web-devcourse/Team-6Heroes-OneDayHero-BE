package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import lombok.Builder;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Builder
public record MissionUpdateServiceRequest(
        Long missionCategoryId,
        Long citizenId,
        Long regionId,
        Double latitude,
        Double longitude,
        MissionInfoServiceRequest missionInfo
) {

    public Mission toEntity(MissionCategory missionCategory, LocalDateTime serverTime) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .citizenId(citizenId)
                .regionId(regionId)
                .location(new Point(longitude, latitude))
                .missionInfo(missionInfo.toVo(serverTime))
                .build();
    }
}
