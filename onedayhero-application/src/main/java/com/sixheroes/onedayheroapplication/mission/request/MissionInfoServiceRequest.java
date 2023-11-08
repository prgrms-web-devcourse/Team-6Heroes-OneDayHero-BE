package com.sixheroes.onedayheroapplication.mission.request;

import com.sixheroes.onedayherodomain.mission.MissionInfo;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record MissionInfoServiceRequest(
        String title,
        String content,
        LocalDate missionDate,
        LocalTime startTime,
        LocalTime endTime,
        LocalTime deadlineTime,
        Integer price
) {

    public MissionInfo toVo(LocalDateTime serverTime) {
        return MissionInfo.builder()
                .title(title)
                .content(content)
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(price)
                .serverTime(serverTime)
                .build();
    }
}
