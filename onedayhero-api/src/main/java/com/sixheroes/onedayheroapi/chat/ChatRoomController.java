package com.sixheroes.onedayheroapi.chat;

import com.sixheroes.onedayheroapi.chat.request.CreateMissionChatRoomRequest;
import com.sixheroes.onedayheroapi.global.response.ApiResponse;
import com.sixheroes.onedayheroapplication.chatroom.ChatRoomService;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/mission-chatrooms")
public class ChatRoomController {

    private static final String CHAT_ROOM_URI_FORMAT = "/api/v1/mission-chatrooms/";
    public ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse<MissionChatRoomResponse>> createChatRoom(
            @RequestBody @Valid CreateMissionChatRoomRequest request
    ) {
        var result = chatRoomService.createChatRoom(request.missionId());
        return ResponseEntity.created(URI.create(CHAT_ROOM_URI_FORMAT + result.id()))
                .body(ApiResponse.created(result));
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ApiResponse<MissionChatRoomResponse>> findByChatRoomId(
            @PathVariable Long chatRoomId
    ) {
        var result = chatRoomService.findOne(chatRoomId);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<MissionChatRoomResponses>> findAll() {
        var result = chatRoomService.findAll();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}
