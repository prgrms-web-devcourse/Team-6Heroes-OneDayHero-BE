package com.sixheroes.onedayheroquerydsl.missionproposal.dto;

import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MissionProposalQueryDto(
    Long missionProposalId,
    Long missionId,
    MissionStatus missionStatus,
    Integer bookmarkCount,
    LocalDateTime missionCreatedAt,
    LocalDate missionDate,
    LocalTime startTime,
    LocalTime endTime,
    Integer price,
    MissionCategoryCode missionCategoryCode,
    String categoryName,
    String si,
    String gu,
    String dong
) {
}
