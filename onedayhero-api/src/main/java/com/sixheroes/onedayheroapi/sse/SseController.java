package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/sse")
@RestController
public class SseController {

    private static final String DUMMY_DATA = "sse";
    private final SseEmitters sseEmitters;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
        HttpServletResponse response,
        @AuthUser Long userId
    ) {
        var sseEmitter = sseEmitters.add(userId);

        response.setHeader(HttpHeaders.CONNECTION, "keep-alive");
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/event-stream;charset=utf-8");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");

        sseEmitters.send(userId, DUMMY_DATA);

        return sseEmitter;
    }
}
