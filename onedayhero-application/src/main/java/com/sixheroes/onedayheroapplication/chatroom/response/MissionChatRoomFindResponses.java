package com.sixheroes.onedayheroapplication.chatroom.response;

import java.util.List;

public record MissionChatRoomFindResponses(
        List<MissionChatRoomFindResponse> missionChatRoomFindResponses
) {

    public static MissionChatRoomFindResponses from(List<MissionChatRoomFindResponse> response) {
        return new MissionChatRoomFindResponses(response);
    }
}
