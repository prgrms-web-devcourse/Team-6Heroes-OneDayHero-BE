package com.sixheroes.onedayheroapplication.missionproposal.repository.dto;

import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MissionProposalQueryDto(
    Long missionProposalId,
    Long missionId,
    String title,
    Integer price,
    LocalDate missionDate,
    LocalTime startTime,
    LocalTime endTime,
    LocalDateTime missionCreatedAt,
    Long categoryId,
    MissionCategoryCode categoryCode,
    String categoryName,
    String si,
    String gu,
    String dong,
    Integer bookmarkCount,
    MissionStatus missionStatus,
    Long bookmarkId,
    LocalDateTime missionProposalCreatedAt
) {
}
