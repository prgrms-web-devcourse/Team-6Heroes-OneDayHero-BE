package com.sixheroes.onedayheroapi.sse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SseEmittersTest {

    @DisplayName("SseEmitter를 추가한다.")
    @Test
    void addSseEmitter() {
        // given
        var sseEmitters = new SseEmitters();
        var userId = 1L;
        var defaultTimeOut = 30 * 60 * 1000L;

        // when
        var sseEmitter = sseEmitters.add(userId);

        // then
        assertThat(sseEmitter.getTimeout()).isEqualTo(defaultTimeOut);
    }
}