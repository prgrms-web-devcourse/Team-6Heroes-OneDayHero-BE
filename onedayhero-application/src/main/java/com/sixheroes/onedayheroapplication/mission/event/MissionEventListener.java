package com.sixheroes.onedayheroapplication.mission.event;

import com.sixheroes.onedayheroapplication.mission.event.dto.MissionCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class MissionEventListener {

    private final MissionEventService missionEventService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyMissionCompleted(
        MissionCompletedEvent missionCompletedEvent
    ) {
        missionEventService.notifyMissionCompleted(missionCompletedEvent);
    }
}
