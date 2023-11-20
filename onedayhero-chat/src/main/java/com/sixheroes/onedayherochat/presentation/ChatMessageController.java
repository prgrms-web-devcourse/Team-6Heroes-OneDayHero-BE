package com.sixheroes.onedayherochat.presentation;

import com.sixheroes.onedayherochat.application.ChatService;
import com.sixheroes.onedayherochat.presentation.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatService chatService;

    @MessageMapping("/chatRooms/{chatRoomId}/chat")
    public void sendMessage(
            @DestinationVariable("chatRoomId") Long chatRoomId,
            ChatMessageRequest message
    ) {
        var serverTime = LocalDateTime.now();
        chatService.send(chatRoomId, message, serverTime);
    }
}
