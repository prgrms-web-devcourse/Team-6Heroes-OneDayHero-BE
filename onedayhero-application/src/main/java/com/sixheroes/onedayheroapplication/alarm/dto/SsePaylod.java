package com.sixheroes.onedayheroapplication.alarm.dto;

import com.sixheroes.onedayheromongodb.alarm.Alarm;
import lombok.Builder;

@Builder
public record SsePaylod(
    String alarmType,
    Long userId,
    Data data
) {

    public static SsePaylod of(
        String alarmType,
        Alarm alarm
    ) {
        var data = new Data(alarm.getTitle(), alarm.getContent());

        return SsePaylod.builder()
            .alarmType(alarmType)
            .userId(alarm.getUserId())
            .data(data)
            .build();
    }

    record Data(
        String title,
        String content
    ) {
    }
}
