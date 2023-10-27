package com.sixheroes.onedayheroapplication.mission.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import lombok.Builder;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionResponse(
        Long id,
        MissionCategoryResponse missionCategory,
        Long citizenId,
        Long regionId,
        Point location,
        MissionInfoResponse missionInfo,
        Integer bookmarkCount,
        String missionStatus
) {
    public MissionResponse(Mission mission) {
        this(
                mission.getId(),
                new MissionCategoryResponse(mission.getMissionCategory()),
                mission.getCitizenId(),
                mission.getRegionId(),
                mission.getLocation(),
                new MissionInfoResponse(mission.getMissionInfo()),
                mission.getBookmarkCount(),
                mission.getMissionStatus().name()
        );
    }

    @Builder
    public record MissionInfoResponse(
            String content,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate missionDate,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime startTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime endTime,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime deadlineTime,

            Integer price
    ) {
        private MissionInfoResponse(MissionInfo missionInfo) {
            this(
                    missionInfo.getContent(),
                    missionInfo.getMissionDate(),
                    missionInfo.getStartTime(),
                    missionInfo.getEndTime(),
                    missionInfo.getDeadlineTime(),
                    missionInfo.getPrice()
            );
        }
    }
}
