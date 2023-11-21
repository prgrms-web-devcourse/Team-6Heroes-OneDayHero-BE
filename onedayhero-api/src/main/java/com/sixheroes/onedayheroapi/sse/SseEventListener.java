package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapplication.alarm.dto.SsePaylod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class SseEventListener {

    private final SseEmitters sseEmitters;

    @EventListener
    public void sendSseEmitter(SsePaylod ssePaylod) {
        sseEmitters.send(
            ssePaylod.userId(),
            "alarm",
            ssePaylod.data()
        );
    }
}
