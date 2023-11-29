package com.sixheroes.onedayheroapplication.missionmatch.event;

import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchReader;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchAction;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchCreateEvent;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchRejectEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionMatchEventService {

    private final MissionMatchReader missionMatchReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void notifyMissionMatchCreate(
        MissionMatchCreateEvent missionMatchCreateEvent
    ) {
        var missionMatchEvent = missionMatchReader.findMissionMatchEvent(missionMatchCreateEvent.missionMatchId());

        var alarmPayload = MissionMatchEventMapper.toAlarmPayload(missionMatchEvent, MissionMatchAction.MISSION_MATCH_CREATE);

        applicationEventPublisher.publishEvent(alarmPayload);
    }

    public void notifyMissionMatchReject(
        MissionMatchRejectEvent missionMatchRejectEvent
    ) {
        var missionMatchEventDto = missionMatchReader.findMissionMatchEvent(missionMatchRejectEvent.missionMatchId());

        var alarmPayload = MissionMatchEventMapper.toAlarmPayload(missionMatchEventDto, MissionMatchAction.MISSION_MATCH_REJECT);

        applicationEventPublisher.publishEvent(alarmPayload);
    }
}
