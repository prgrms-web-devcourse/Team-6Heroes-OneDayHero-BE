package com.sixheroes.onedayheroapplication.missionproposal.event;

import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalReader;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalAction;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalApproveEvent;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalCreateEvent;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalRejectEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MissionProposalEventService {

    private final MissionProposalReader missionProposalReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void notifyMissionProposalCreate(MissionProposalCreateEvent missionProposalCreateEvent) {
        var missionProposalId = missionProposalCreateEvent.missionProposalId();
        var missionProposalCreateEventDto = missionProposalReader.findCreateEvent(missionProposalId);

        var alarmPayload = MissionProposalEventMapper.toAlarmPayload(missionProposalCreateEventDto);

        applicationEventPublisher.publishEvent(alarmPayload);
    }

    public void notifyMissionProposalApprove(MissionProposalApproveEvent missionProposalApproveEvent) {
        var missionProposalId = missionProposalApproveEvent.missionProposalId();
        var missionProposalUpdateEventDto = missionProposalReader.findUpdateEvent(missionProposalId);

        var alarmPayload = MissionProposalEventMapper.toAlarmPayload(missionProposalUpdateEventDto, MissionProposalAction.MISSION_PROPOSAL_APPROVE);

        applicationEventPublisher.publishEvent(alarmPayload);
    }

    public void notifyMissionProposalReject(MissionProposalRejectEvent missionProposalRejectEvent) {
        var missionProposalId = missionProposalRejectEvent.missionProposalId();
        var missionProposalUpdateEventDto = missionProposalReader.findUpdateEvent(missionProposalId);

        var alarmPayload = MissionProposalEventMapper.toAlarmPayload(missionProposalUpdateEventDto, MissionProposalAction.MISSION_PROPOSAL_REJECT);

        applicationEventPublisher.publishEvent(alarmPayload);
    }
}