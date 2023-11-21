package com.sixheroes.onedayheroapplication.alarm;

import com.sixheroes.onedayheroapplication.alarm.dto.AlarmPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class AlarmEventListener {

    private final AlarmService alarmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyClient(
        AlarmPayload alarmPayload
    ) {
        alarmService.notifyClient(alarmPayload);
    }
}
