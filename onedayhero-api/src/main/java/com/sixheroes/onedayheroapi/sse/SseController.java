package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/sse")
@RestController
public class SseController {

    private static final String DUMMY_DATA_NAME = "success";
    private static final String DUMMY_DATA = "Sse subscribe success";
    private final SseEmitters sseEmitters;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        @AuthUser Long userId
    ) {
        var sseEmitter = sseEmitters.add(userId);
        try {
            sseEmitter.send("sse emitter 응답 보내기");
            log.info("sse emitter controller에서 응답 보내영");
        } catch (IOException e) {
            log.error("보내는데 오류났어요!");
            throw new RuntimeException(e);
        }
        log.info("request : {}", httpServletRequest);
        log.info("response : {}", httpServletResponse);
        sseEmitters.send(userId, DUMMY_DATA_NAME, DUMMY_DATA);
        return sseEmitter;
    }
}
