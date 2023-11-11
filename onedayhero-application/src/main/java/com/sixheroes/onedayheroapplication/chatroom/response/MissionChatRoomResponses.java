package com.sixheroes.onedayheroapplication.chatroom.response;

import java.util.List;

public record MissionChatRoomResponses(
        List<MissionChatRoomResponse> missionChatRoomResponses
) {

    public static MissionChatRoomResponses from(
            List<MissionChatRoomResponse> missionChatRoomResponses
    ) {
        return new MissionChatRoomResponses(missionChatRoomResponses);
    }
}
