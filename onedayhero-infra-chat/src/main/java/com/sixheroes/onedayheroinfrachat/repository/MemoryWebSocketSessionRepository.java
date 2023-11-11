package com.sixheroes.onedayheroinfrachat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemoryWebSocketSessionRepository implements SocketSessionRepository {

    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId 별로 2개의 WebSocketSession 을 유지한 채로 가지고 있을 수 있다.
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();

    @Override
    public Long save(
            WebSocketSession webSocketSession
    ) {
        sessions.add(webSocketSession);
        var sessionId = webSocketSession.getId();
        log.info("{} 세션이 저장되었습니다.", sessionId);

        return Long.parseLong(webSocketSession.getId());
    }

    @Override
    public void createChatRoom(Long chatRoomId) {
        chatRoomSessionMap.put(chatRoomId, new HashSet<>());
    }

    @Override
    public boolean isExistChatRoom(
            Long chatRoomId
    ) {
        var webSocketSessions = chatRoomSessionMap.get(chatRoomId);

        if (webSocketSessions.isEmpty()) {
            log.info("{} 채팅방과 연결 된 세션을 조회하였지만 없었습니다.", chatRoomId);
            return false;
        }

        return true;
    }

    @Override
    public Set<WebSocketSession> getByChatRoomId(Long chatRoomId) {
        return chatRoomSessionMap.get(chatRoomId);
    }

    @Override
    public void connectSessionInChatRoom(
            WebSocketSession webSocketSession,
            Set<WebSocketSession> chatRoomSessions
    ) {
        if (chatRoomSessions.size() == 2) {
            log.debug("이미 채팅방에 두 명의 유저가 접속해있는 상태입니다.");
            return;
        }

        chatRoomSessions.add(webSocketSession);
    }

    @Override
    public void remove(WebSocketSession session) {
        sessions.remove(session);
    }
}
