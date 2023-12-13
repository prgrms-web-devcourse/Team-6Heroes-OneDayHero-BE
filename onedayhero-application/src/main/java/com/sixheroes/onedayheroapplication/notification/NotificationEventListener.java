package com.sixheroes.onedayheroapplication.notification;

import com.sixheroes.onedayheroapplication.notification.dto.AlarmPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void notifyClient(
        AlarmPayload alarmPayload
    ) {
        notificationService.notifyClient(alarmPayload);
    }
}
