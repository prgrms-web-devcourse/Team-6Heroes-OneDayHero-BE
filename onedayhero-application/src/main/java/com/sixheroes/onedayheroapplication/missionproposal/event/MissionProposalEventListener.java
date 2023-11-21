package com.sixheroes.onedayheroapplication.missionproposal.event;

import com.sixheroes.onedayheroapplication.missionproposal.event.dto.MissionProposalCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class MissionProposalEventListener {

    private final MissionProposalEventService missionProposalEventService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyMissionProposalCreate(MissionProposalCreateEvent missionProposalEvent) {
        missionProposalEventService.notifyMissionProposalCreate(missionProposalEvent);
    }
}
