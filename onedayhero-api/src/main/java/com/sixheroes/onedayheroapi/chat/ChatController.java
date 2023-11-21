package com.sixheroes.onedayheroapi.chat;

import com.sixheroes.onedayheroapi.global.argumentsresolver.authuser.AuthUser;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayherochat.application.ChatService;
import com.sixheroes.onedayherochat.application.response.ChatMessageApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<List<ChatMessageApiResponse>>> enterChatRoom(
            @AuthUser Long userId,
            @PathVariable Long chatRoomId
    ) {
        var result = chatService.findMessageByChatRoomId(chatRoomId);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
