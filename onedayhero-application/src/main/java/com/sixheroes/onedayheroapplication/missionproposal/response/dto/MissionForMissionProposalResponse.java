package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayheroapplication.missionproposal.repository.dto.MissionProposalQueryDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MissionForMissionProposalResponse(
        Long id,

        String status,

        Integer bookmarkCount,

        Boolean isBookmarked,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdAt,

        RegionForMissionProposalResponse region,

        MissionCategoryForMissionProposalResponse missionCategory,

        MissionInfoForMissionProposalResponse missionInfo,

        String imagePath
) {

    public static MissionForMissionProposalResponse from(
        MissionProposalQueryDto missionProposalQueryDto,
        String imagePath,
        Boolean isBookmarked
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
            .imagePath(imagePath)
            .isBookmarked(isBookmarked)
            .build();
    }
}
