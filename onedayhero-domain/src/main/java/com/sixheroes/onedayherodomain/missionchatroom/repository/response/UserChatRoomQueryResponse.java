package com.sixheroes.onedayherodomain.missionchatroom.repository.response;

public record UserChatRoomQueryResponse(
        Long chatRoomId,
        Long receiverId,
        Long missionId,
        String title,
        String nickName,
        String path,
        Integer headCount
) {
}
