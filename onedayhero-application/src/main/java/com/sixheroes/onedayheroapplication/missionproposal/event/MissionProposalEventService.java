package com.sixheroes.onedayheroapplication.missionproposal.event;

import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalReader;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MissionProposalEventService {

    private final MissionProposalReader missionProposalReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional(readOnly = true)
    public void notifyMissionProposalCreate(
            MissionProposalCreateEvent missionProposalCreateEvent
    ) {
        var missionProposalId = missionProposalCreateEvent.missionProposalId();
        var missionProposalCreateEventDto = missionProposalReader.findCreateEvent(missionProposalId);

        var alarmPayload = MissionProposalEventMapper.toAlarmPayload(missionProposalCreateEventDto);

        applicationEventPublisher.publishEvent(alarmPayload);
    }
}