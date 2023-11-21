package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import com.sixheroes.onedayherochat.application.repository.request.MissionChatRoomRedisRequest;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

@Transactional
class ChatRoomServiceTest extends IntegrationApplicationTest {

    @DisplayName("유저는 채팅방을 생성 할 수 있다.")
    @Test
    void test() {
        // given
        var missionId = 1L;
        var userIds = List.of(1L, 2L);

        var request = CreateMissionChatRoomServiceRequest.builder()
                .missionId(missionId)
                .userIds(userIds)
                .build();

        var missionChatRoom = MissionChatRoom.createMissionChatRoom(request.missionId(), request.userIds());
        var redisRequest = MissionChatRoomRedisRequest.from(missionChatRoom);

        given(missionChatRoomRedisRepository.create(any(MissionChatRoomRedisRequest.class)))
                .willReturn(redisRequest);

        // when
        var chatRoom = chatRoomService.createChatRoom(request);

        // then
        assertThat(chatRoom.missionId()).isEqualTo(missionId);
    }
}