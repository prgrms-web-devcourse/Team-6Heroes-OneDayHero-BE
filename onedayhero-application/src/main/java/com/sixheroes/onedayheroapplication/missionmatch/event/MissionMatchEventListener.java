package com.sixheroes.onedayheroapplication.missionmatch.event;

import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchCreateEvent;
import com.sixheroes.onedayheroapplication.missionmatch.event.dto.MissionMatchRejectEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class MissionMatchEventListener {

    private final MissionMatchEventService missionMatchEventService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyMissionMatchCreate(
        MissionMatchCreateEvent missionMatchCreateEvent
    ) {
        missionMatchEventService.notifyMissionMatchCreate(missionMatchCreateEvent);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyMissionMatchReject(
        MissionMatchRejectEvent missionMatchRejectEvent
    ) {
        missionMatchEventService.notifyMissionMatchReject(missionMatchRejectEvent);
    }
}
