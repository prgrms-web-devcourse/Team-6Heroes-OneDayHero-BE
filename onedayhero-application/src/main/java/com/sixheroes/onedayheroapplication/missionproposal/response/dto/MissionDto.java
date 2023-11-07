package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionDto(
    Long missionId,

    String missionStatus,

    Integer bookmarkCount,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime missionCreatedAt,

    RegionDto region,

    MissionCategoryDto missionCategory,

    MissionInfoDto missionInfo
) {
}
