package com.sixheroes.onedayheroapi.chat;

import com.sixheroes.onedayheroapi.chat.request.ChatMessage;
import com.sixheroes.onedayheroapplication.chat.ChatHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private static final String CHAT_MESSAGE_URI = "/queue/chat/room/";
    private final ChatHandler chatHandler;
    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/chat/message")
    public void sendMessage(
//            @DestinationVariable("id") Long id,
            ChatMessage message
    ) {
        var handledMessage = chatHandler.handleChatMessageByChatType(message.toHandler());
        log.info("전송되는 메시지 : {}", handledMessage);
        messageTemplate.convertAndSend(CHAT_MESSAGE_URI + message.id(), handledMessage);
    }
}
