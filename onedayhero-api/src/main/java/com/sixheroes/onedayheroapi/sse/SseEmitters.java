package com.sixheroes.onedayheroapi.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitters {

    private static final Long DEFAULT_TIMEOUT = 30 * 60 * 1000L; // 30분
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        this.emitters.put(userId, emitter);
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(userId);
        });
        return emitter;
    }

    public void send(
        Long userId,
        String name,
        Object data
    ) {
        var sseEmitter = get(userId);
        try {
            sseEmitter.send(SseEmitter.event()
                .name(name)
                .data(data)
            );
        } catch (IOException e) {
            log.error("SSE를 보내는 과정에서 오류가 발생했습니다.");
        }
    }

    private SseEmitter get(
        Long userId
    ) {
        var sseEmitter = emitters.get(userId);
        if (sseEmitter == null) {
            log.debug("SSE를 구독하지 않은 유저입니다. userId : {}", userId);
        }
        return sseEmitter;
    }
}