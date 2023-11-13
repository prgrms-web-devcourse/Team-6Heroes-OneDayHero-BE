package com.sixheroes.onedayheroapplication.chat;

import com.sixheroes.onedayheroapplication.chat.request.HandledChatMessage;
import com.sixheroes.onedayherodomain.missionchatroom.ChatType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatHandler {

    public HandledChatMessage handleChatMessageByChatType(
            HandledChatMessage message
    ) {
        log.info("message 도착 {}", message);
        var chatType = ChatType.findByName(message.type());
        if (chatType.isEnter()) {
            log.info("입장에 대한 chatMessage가 생성되었습니다. id : {}", message.id());
            return HandledChatMessage.createEnterHandledChatMessage(message);
        }

        return message;
    }
}
