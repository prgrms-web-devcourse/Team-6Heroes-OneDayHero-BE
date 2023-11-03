package com.sixheroes.onedayheroapplication.missionrequest.dto;

import com.sixheroes.onedayheroquerydsl.missionrequest.dto.MissionRequestQueryDto;

public record MissionRequestDto(
    Long missionRequestId,
    MissionDto mission
) {
    public static MissionRequestDto from(
            MissionRequestQueryDto missionRequestQueryDto
    ) {
        var regionDto = RegionDto.builder()
                .si(missionRequestQueryDto.si())
                .gu(missionRequestQueryDto.gu())
                .dong(missionRequestQueryDto.dong())
                .build();

        var missionInfoDto = MissionInfoDto.builder()
                .missionDate(missionRequestQueryDto.missionDate())
                .startTime(missionRequestQueryDto.startTime())
                .endTime(missionRequestQueryDto.endTime())
                .price(missionRequestQueryDto.price())
                .build();

        var missionCategoryDto = MissionCategoryDto.builder()
                .categoryName(missionRequestQueryDto.categoryName())
                .missionCategoryCode(missionRequestQueryDto.missionCategoryCode().name())
                .build();

        var missionDto = MissionDto.builder()
                .missionId(missionRequestQueryDto.missionId())
                .missionStatus(missionRequestQueryDto.missionStatus().name())
                .missionCreatedAt(missionRequestQueryDto.missionCreatedAt())
                .bookmarkCount(missionRequestQueryDto.bookmarkCount())
                .region(regionDto)
                .missionInfo(missionInfoDto)
                .missionCategory(missionCategoryDto)
                .build();

        return new MissionRequestDto(missionRequestQueryDto.missionRequestId(), missionDto);
    }
}
