package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MissionUpdateServiceRequest(
        Long missionCategoryId,
        Long citizenId,
        Long regionId,
        Double latitude,
        Double longitude,
        MissionInfoServiceRequest missionInfo,
        List<S3ImageUploadServiceRequest> imageFiles
) {

    public Mission toEntity(MissionCategory missionCategory, LocalDateTime serverTime) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .citizenId(citizenId)
                .regionId(regionId)
                .location(Mission.createPoint(longitude, latitude))
                .missionInfo(missionInfo.toVo(serverTime))
                .build();
    }
}
