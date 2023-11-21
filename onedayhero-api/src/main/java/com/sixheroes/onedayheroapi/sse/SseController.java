package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/sse/")
@RestController
public class SseController {

    private static final String DUMMY_DATA_NAME = "success";
    private static final String DUMMY_DATA = "Sse subscribe success";
    private final SseEmitters sseEmitters;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
        @AuthUser Long userId
    ) {
        var sseEmitter = sseEmitters.add(userId);
        sseEmitters.send(userId, DUMMY_DATA_NAME, DUMMY_DATA);
        return ResponseEntity.ok(sseEmitter);
    }
}
