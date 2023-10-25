package com.sixheroes.onedayheroapplication.mission.response;

import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private record MissionInfoResponse(
            String content,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
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
