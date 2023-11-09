package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionForMissionProposalResponse(
    Long id,

    String status,

    Integer bookmarkCount,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdAt,

    RegionForMissionProposalResponse region,

    MissionCategoryForMissionProposalResponse missionCategory,

    MissionInfoForMissionProposalResponse missionInfo
) {

    public static MissionForMissionProposalResponse from(
        MissionProposalQueryDto missionProposalQueryDto
    ) {
        var region = RegionForMissionProposalResponse.from(missionProposalQueryDto);
        var missionCategory = MissionCategoryForMissionProposalResponse.from(missionProposalQueryDto);
        var missionInfo = MissionInfoForMissionProposalResponse.from(missionProposalQueryDto);

        return MissionForMissionProposalResponse.builder()
            .id(missionProposalQueryDto.missionId())
            .status(missionProposalQueryDto.missionStatus().name())
            .createdAt(missionProposalQueryDto.missionCreatedAt())
            .region(region)
            .missionCategory(missionCategory)
            .missionInfo(missionInfo)
            .build();
    }
}
