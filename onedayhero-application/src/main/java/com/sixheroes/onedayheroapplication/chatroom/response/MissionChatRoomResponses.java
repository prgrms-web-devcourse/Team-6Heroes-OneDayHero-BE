package com.sixheroes.onedayheroapplication.chatroom.response;

import java.util.List;

public record MissionChatRoomResponses(
        List<MissionChatRoomCreateResponse> missionCreateChatRoomResponse
) {

    public static MissionChatRoomResponses from(
            List<MissionChatRoomCreateResponse> missionCreateChatRoomResponse
    ) {
        return new MissionChatRoomResponses(missionCreateChatRoomResponse);
    }
}
