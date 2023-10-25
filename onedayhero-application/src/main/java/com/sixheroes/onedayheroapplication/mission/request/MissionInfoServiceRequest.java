package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayherodomain.mission.MissionInfo;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record MissionInfoServiceRequest(
        String content,
        LocalDate missionDate,
        LocalTime startTime,
        LocalTime endTime,
        LocalTime deadlineTime,
        Integer price
) {

    public MissionInfo toVo() {
        return MissionInfo.builder()
                .content(content)
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(price)
                .build();
    }
}
