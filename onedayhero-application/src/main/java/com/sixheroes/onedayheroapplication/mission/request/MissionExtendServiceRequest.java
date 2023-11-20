package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayherodomain.mission.MissionInfo;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record MissionExtendServiceRequest(
        Long citizenId,
        LocalDate missionDate,
        LocalTime startTime,
        LocalTime endTime,
        LocalDateTime deadlineTime
) {

    public MissionInfo toVo(
            MissionInfo missionInfo,
            LocalDateTime serverTime
    ) {
        return missionInfo.extend(missionDate, startTime, endTime, deadlineTime, serverTime);
    }
}
