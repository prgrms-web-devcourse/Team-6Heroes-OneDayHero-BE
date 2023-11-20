package com.sixheroes.onedayheroapi.chatroom;

import com.sixheroes.onedayheroapi.chatroom.request.ChatRoomExitRequest;
import com.sixheroes.onedayheroapi.chatroom.request.CreateMissionChatRoomRequest;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.chatroom.ChatRoomService;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomCreateResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomExitResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomFindResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-rooms")
public class ChatRoomController {

    private static final String CHAT_ROOM_URI_FORMAT = "/api/v1/chat-rooms/";
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionChatRoomCreateResponse>> createChatRoom(
            @RequestBody @Valid CreateMissionChatRoomRequest request
    ) {
        var result = chatRoomService.createChatRoom(request.toService());
        return ResponseEntity.created(URI.create(CHAT_ROOM_URI_FORMAT + result.id()))
                .body(ApiResponse.created(result));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<MissionChatRoomFindResponses>> findByChatRoomId(
            @PathVariable Long userId
    ) {
        var result = chatRoomService.findJoinedChatRoom(userId);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{chatRoomId}/exit")
    public ResponseEntity<ApiResponse<MissionChatRoomExitResponse>> exitChatRoom(
            @PathVariable Long chatRoomId,
            @Valid @RequestBody ChatRoomExitRequest request
    ) {
        var missionChatRoomResponse = chatRoomService.exitChatRoom(chatRoomId, request.userId());

        return ResponseEntity.ok(ApiResponse.ok(missionChatRoomResponse));
    }
}
