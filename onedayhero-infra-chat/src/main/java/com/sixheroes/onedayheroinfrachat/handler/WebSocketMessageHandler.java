package com.sixheroes.onedayheroinfrachat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixheroes.onedayheroinfrachat.domain.ChatMessageType;
import com.sixheroes.onedayheroinfrachat.handler.request.ChatMessageInfraRequest;
import com.sixheroes.onedayheroinfrachat.repository.SocketSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final SocketSessionRepository socketSessionRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        socketSessionRepository.save(session);
        log.info("{} 세션이 연결되었습니다.", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        log.info("payload : {} ", payload);
        var chatMessageInfraRequest = objectMapper.readValue(payload, ChatMessageInfraRequest.class);

        var chatRoomId = chatMessageInfraRequest.chatRoomId();
        if (!socketSessionRepository.isExistChatRoom(chatRoomId)) {
            socketSessionRepository.createChatRoom(chatRoomId);
        }

        var chatRoomSessions = socketSessionRepository.getByChatRoomId(chatRoomId);

        var chatType = ChatMessageType.findByName(chatMessageInfraRequest.chatType());

        if (chatType.isEnter()) {
            socketSessionRepository.connectSessionInChatRoom(session, chatRoomSessions);
        }

        sendMessageToChatRoom(chatMessageInfraRequest, chatRoomSessions);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        socketSessionRepository.remove(session);
    }

    private void sendMessageToChatRoom(ChatMessageInfraRequest request, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream()
                .forEach(sess -> sendMessage(sess, request));
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
