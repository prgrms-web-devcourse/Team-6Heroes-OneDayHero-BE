package com.sixheroes.onedayherodomain.missionchatroom.repository.response;

import com.sixheroes.onedayherodomain.mission.MissionStatus;

public record UserChatRoomQueryResponse(
        Long chatRoomId,
        Long receiverId,
        Long missionId,
        MissionStatus missionStatus,
        String title,
        String nickName,
        String path,
        Integer headCount
) {
}
