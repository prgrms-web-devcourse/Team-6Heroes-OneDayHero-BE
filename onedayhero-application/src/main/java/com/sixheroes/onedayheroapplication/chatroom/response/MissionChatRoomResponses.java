package com.sixheroes.onedayheroapplication.chatroom.response;

import java.util.List;

public record MissionChatRoomResponses(
        List<MissionChatRoomCreateResponse> missionCreateChatRoomRespons
) {

    public static MissionChatRoomResponses from(
            List<MissionChatRoomCreateResponse> missionCreateChatRoomRespons
    ) {
        return new MissionChatRoomResponses(missionCreateChatRoomRespons);
    }
}
