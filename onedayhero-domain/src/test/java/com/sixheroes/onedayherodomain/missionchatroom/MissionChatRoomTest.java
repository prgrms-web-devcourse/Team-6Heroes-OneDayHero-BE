package com.sixheroes.onedayherodomain.missionchatroom;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionChatRoomTest {

    @DisplayName("유저는 채팅방을 생성 할 수 있다.")
    @Test
    void createChatRoom() {
        // given
        var missionId = 1L;
        var userIds = List.of(1L, 2L);

        // when
        var missionChatRoom = MissionChatRoom.createMissionChatRoom(missionId, userIds);

        // then
        assertThat(missionChatRoom.getMissionId()).isEqualTo(missionId);
        assertThat(missionChatRoom.getUserMissionChatRooms()).hasSize(2);
        assertThat(missionChatRoom.getUserMissionChatRooms().get(0).getUserId()).isEqualTo(1L);
        assertThat(missionChatRoom.getUserMissionChatRooms().get(1).getUserId()).isEqualTo(2L);
    }

    @DisplayName("유저가 채팅방을 생성 할 때에는 두 명의 인원이 필요하다.")
    @Test
    void createChatRoomWithInvalidUserCount() {
        // given
        var missionId = 1L;
        var userIds = List.of(1L);

        // when & then
        assertThatThrownBy(() -> MissionChatRoom.createMissionChatRoom(missionId, userIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.T_001.name());
    }
}