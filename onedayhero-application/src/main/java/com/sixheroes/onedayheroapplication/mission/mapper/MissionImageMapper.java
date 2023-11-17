package com.sixheroes.onedayheroapplication.mission.mapper;

import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayherodomain.mission.MissionImage;

public final class MissionImageMapper {

    private MissionImageMapper() {

    }

    public static MissionImage createMissionImage(S3ImageUploadServiceResponse response) {
        return MissionImage.createMissionImage(response.originalName(), response.uniqueName(), response.path());
    }
}
