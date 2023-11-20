package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record MissionCreateServiceRequest(
        Long missionCategoryId,
        Long citizenId,
        Long regionId,
        Double latitude,
        Double longitude,
        List<S3ImageUploadServiceRequest> imageFiles,
        MissionInfoServiceRequest missionInfo
) {

    public Mission toEntity(
            MissionCategory missionCategory,
            LocalDateTime serverTime
    ) {
        return Mission.createMission(
                missionCategory,
                citizenId,
                regionId,
                longitude,
                latitude,
                missionInfo.toVo(serverTime)
        );
    }
}
