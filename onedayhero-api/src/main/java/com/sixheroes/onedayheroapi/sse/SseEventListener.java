package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapplication.notification.dto.SsePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SseEventListener {

    private final SseEmitters sseEmitters;

    @EventListener
    public void sendSseEmitter(SsePayload ssePaylod) {
        sseEmitters.send(
            ssePaylod.userId(),
            ssePaylod.data()
        );
    }
}
