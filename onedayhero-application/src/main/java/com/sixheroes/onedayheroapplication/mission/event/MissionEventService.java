package com.sixheroes.onedayheroapplication.mission.event;

import com.sixheroes.onedayheroapplication.mission.MissionReader;
import com.sixheroes.onedayheroapplication.mission.converter.MissionEventMapper;
import com.sixheroes.onedayheroapplication.mission.event.dto.MissionCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionEventService {

    private final MissionReader missionReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void notifyMissionCompleted(
        MissionCompletedEvent missionCompletedEvent
    ) {
        var missionId = missionCompletedEvent.missionId();
        var missionCompletedEventQueryResponse = missionReader.findMissionCompletedEvent(missionId);

        var alarmPayload = MissionEventMapper.toAlarmPayload(missionCompletedEventQueryResponse);

        applicationEventPublisher.publishEvent(alarmPayload);
    }
}
