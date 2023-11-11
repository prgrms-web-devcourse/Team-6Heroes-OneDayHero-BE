package com.sixheroes.onedayheroinfrachat.repository;

import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface SocketSessionRepository {

    Long save(WebSocketSession webSocketSession);

    boolean isExistChatRoom(Long chatRoomId);

    Set<WebSocketSession> getByChatRoomId(Long chatRoomId);

    void createChatRoom(Long chatRoomId);

    void connectSessionInChatRoom(WebSocketSession webSocketSession, Set<WebSocketSession> chatRoomSessions);

    void remove(WebSocketSession sessions);
}
