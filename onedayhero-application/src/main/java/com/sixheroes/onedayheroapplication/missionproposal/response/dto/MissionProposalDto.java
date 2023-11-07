package com.sixheroes.onedayheroapplication.missionproposal.response.dto;

import com.sixheroes.onedayheroquerydsl.missionproposal.dto.MissionProposalQueryDto;

public record MissionProposalDto(
    Long missionProposalId,
    MissionDto mission
) {
    public static MissionProposalDto from(
            MissionProposalQueryDto missionProposalQueryDto
    ) {
        var regionDto = RegionDto.builder()
            .si(missionProposalQueryDto.si())
            .gu(missionProposalQueryDto.gu())
            .dong(missionProposalQueryDto.dong())
            .build();

        var missionInfoDto = MissionInfoDto.builder()
            .missionTitle(missionProposalQueryDto.missionTitle())
            .missionDate(missionProposalQueryDto.missionDate())
            .startTime(missionProposalQueryDto.startTime())
            .endTime(missionProposalQueryDto.endTime())
            .price(missionProposalQueryDto.price())
            .build();

        var missionCategoryDto = MissionCategoryDto.builder()
            .categoryName(missionProposalQueryDto.categoryName())
            .missionCategoryCode(missionProposalQueryDto.missionCategoryCode().name())
            .build();

        var missionDto = MissionDto.builder()
            .missionId(missionProposalQueryDto.missionId())
            .missionStatus(missionProposalQueryDto.missionStatus().name())
            .missionCreatedAt(missionProposalQueryDto.missionCreatedAt())
            .bookmarkCount(missionProposalQueryDto.bookmarkCount())
            .region(regionDto)
            .missionInfo(missionInfoDto)
            .missionCategory(missionCategoryDto)
            .build();

        return new MissionProposalDto(missionProposalQueryDto.missionProposalId(), missionDto);
    }
}
