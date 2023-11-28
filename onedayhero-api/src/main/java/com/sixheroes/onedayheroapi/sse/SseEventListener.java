package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapplication.alarm.dto.SsePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class SseEventListener {

    private final SseEmitters sseEmitters;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendSseEmitter(SsePayload ssePaylod) {
        sseEmitters.send(
            ssePaylod.userId(),
            "alarm",
            ssePaylod.data()
        );
    }
}
