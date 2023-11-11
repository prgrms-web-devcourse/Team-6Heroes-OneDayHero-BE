package com.sixheroes.onedayheroapi.chat;

import com.sixheroes.onedayheroapi.chat.request.ChatMessage;
import com.sixheroes.onedayheroapplication.chat.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private static final String CHAT_MESSAGE_URI = "/sub/chats/";
    private final ChatHandler chatHandler;
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/chats/messages/{chatRoomId}")
    public void sendMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            ChatMessage message
    ) {
        var handledMessage = chatHandler.handleChatMessageByChatType(message.toHandler());

        messageTemplate.convertAndSend(CHAT_MESSAGE_URI + chatRoomId, handledMessage);
    }
}
