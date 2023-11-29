package com.sixheroes.onedayherochat.application.mapper;

import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;

public final class ChatMessageMapper {

    private final static String LEAVE_MESSAGE_FORMAT = "[알림] %s님이 나가셨습니다.";

    private ChatMessageMapper() {

    }

    public static ChatMessageRequest toLeaveMessage(
            ChatMessageRequest request
    ) {
        var leaveMessage = String.format(LEAVE_MESSAGE_FORMAT, request.senderNickName());
        return ChatMessageRequest.createLeaveMessage(request, leaveMessage);
    }
}
