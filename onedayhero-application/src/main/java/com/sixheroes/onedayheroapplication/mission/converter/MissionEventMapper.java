package com.sixheroes.onedayheroapplication.mission.converter;

import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.mission.event.dto.MissionEventAction;
import com.sixheroes.onedayheroapplication.mission.repository.response.MissionCompletedEventQueryResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class MissionEventMapper {

    public static AlarmPayload toAlarmPayload(
        MissionCompletedEventQueryResponse missionCompletedEventQueryResponse
    ) {
        return AlarmPayload.builder()
            .alarmType(MissionEventAction.MISSION_COMPLETED.name())
            .userId(missionCompletedEventQueryResponse.heroId())
            .data(missionCompletedEventQueryResponse.toMap())
            .build();
    }
}
