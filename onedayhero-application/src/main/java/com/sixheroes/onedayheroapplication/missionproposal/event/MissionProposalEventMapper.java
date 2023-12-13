package com.sixheroes.onedayheroapplication.missionproposal.event;


import com.sixheroes.onedayheroapplication.notification.dto.AlarmPayload;
import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalAction;
import com.sixheroes.onedayherodomain.missionproposal.repository.dto.MissionProposalCreateEventDto;
import com.sixheroes.onedayherodomain.missionproposal.repository.dto.MissionProposalUpdateEventDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalAction.MISSION_PROPOSAL_CREATE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MissionProposalEventMapper {

    public static AlarmPayload toAlarmPayload(
        MissionProposalCreateEventDto missionProposalCreateEventDto
    ) {
        return AlarmPayload.builder()
            .alarmType(MISSION_PROPOSAL_CREATE.name())
            .userId(missionProposalCreateEventDto.heroId())
            .data(missionProposalCreateEventDto.toMap())
            .build();
    }

    public static AlarmPayload toAlarmPayload(
            MissionProposalUpdateEventDto missionProposalUpdateEventDto,
            MissionProposalAction missionProposalAction
    ) {
        return AlarmPayload.builder()
                .alarmType(missionProposalAction.name())
                .userId(missionProposalUpdateEventDto.citizenId())
                .data(missionProposalUpdateEventDto.toMap())
                .build();
    }
}
