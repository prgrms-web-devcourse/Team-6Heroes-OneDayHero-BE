package com.sixheroes.onedayheromongo.chat.application;

import com.sixheroes.onedayheromongo.chat.repository.ChatMessageRepository;
import com.sixheroes.onedayheromongo.chat.request.MongoChatMessage;
import com.sixheroes.onedayheromongo.chat.response.ChatMessageResponse;
import com.sixheroes.onedayheromongo.chat.util.UUIDCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatMongoService {

    private final ChatMessageRepository chatMessageRepository;

    public void save(
            MongoChatMessage mongoChatMessage,
            LocalDateTime serverTime
    ) {
        var id = UUIDCreator.createUUID();
        var chatMessage = mongoChatMessage.toEntity(id, serverTime);

        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageResponse> findAllByChatRoomId(
            Long chatRoomId
    ) {
        var chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);

        return chatMessages.stream()
                .map(ChatMessageResponse::from)
                .toList();
    }
}
