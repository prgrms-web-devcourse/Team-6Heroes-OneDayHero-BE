package com.sixheroes.onedayherodomain.missionchatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ChatType {

    ENTER("입장"),
    CHAT("채팅");

    private final String description;

    public static ChatType findByName(String name) {
        return Arrays.stream(values())
                .filter((messageType) -> messageType.name().equals(name))
                .findAny()
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHAT_TYPE));
    }

    public boolean isEnter() {
        return this == ENTER;
    }

    public boolean isChat() {
        return this == CHAT;
    }
}
