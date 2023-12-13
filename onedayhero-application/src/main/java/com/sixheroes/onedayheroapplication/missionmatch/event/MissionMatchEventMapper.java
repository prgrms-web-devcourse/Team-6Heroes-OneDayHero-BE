package com.sixheroes.onedayheroapplication.missionmatch.event;


import com.sixheroes.onedayheroapplication.notification.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchAction;
import com.sixheroes.onedayherodomain.missionmatch.repository.dto.MissionMatchEventDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MissionMatchEventMapper {

    public static AlarmPayload toAlarmPayload(
        MissionMatchEventDto missionMatchEventDto,
        MissionMatchAction missionMatchAction
    ) {
        return AlarmPayload.builder()
            .alarmType(missionMatchAction.name())
            .userId(missionMatchEventDto.receiverId())
            .data(missionMatchEventDto.toMap())
            .build();
    }
}
