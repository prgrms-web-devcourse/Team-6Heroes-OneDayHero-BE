package com.sixheroes.onedayheroinfrachat.domain;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ChatMessageType {

    ENTER("입장"),
    CHAT("채팅");

    private final String description;

    public static ChatMessageType findByName(String name) {
        return Arrays.stream(values())
                .filter((messageType) -> messageType.name().equals(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.T_001.name()));
    }

    public boolean isEnter() {
        return this == ENTER;
    }

    public boolean isChat() {
        return this == CHAT;
    }
}
