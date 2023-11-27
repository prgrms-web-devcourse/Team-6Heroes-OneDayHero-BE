package com.sixheroes.onedayheroapplication.alarm.dto;

import com.sixheroes.onedayheromongo.alarm.Alarm;
import lombok.Builder;

@Builder
public record SsePayload(
    String alarmType,
    Long userId,
    Data data
) {

    public static SsePayload of(
        String alarmType,
        Alarm alarm
    ) {
        var data = new Data(alarm.getTitle(), alarm.getContent());

        return SsePayload.builder()
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
