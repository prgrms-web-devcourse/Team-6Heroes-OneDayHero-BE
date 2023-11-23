package com.sixheroes.onedayheroapplication.chatroom.repository;


import com.sixheroes.onedayheroapplication.IntegrationQueryDslTest;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.repository.MissionChatRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserMissionChatRoomQueryRepositoryTest extends IntegrationQueryDslTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionChatRoomRepository missionChatRoomRepository;

    @Autowired
    private UserMissionChatRoomQueryRepository userMissionChatRoomQueryRepository;

    @DisplayName("현재 유저가 입장해있는 채팅방을 조회 할 수 있다.")
    @Test
    void findUserChatRoomWithJoined() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var citizenId = 1L;
        var regionId = 1L;

        var missionInfo = MissionInfo.builder()
                .title("미션 제목")
                .content("미션 내용")
                .missionDate(LocalDate.of(2023, 11, 15))
                .startTime(LocalTime.of(9, 0, 0))
                .endTime(LocalTime.of(9, 30, 0))
                .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 15),
                        LocalTime.of(5, 0, 0)
                ))
                .serverTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 14),
                        LocalTime.NOON
                ))
                .price(10000)
                .build();

        var mission = Mission.createMission(
                missionCategory,
                citizenId,
                regionId,
                123.45,
                123.45,
                missionInfo
        );

        var savedMission = missionRepository.save(mission);

        var missionChatRoom = MissionChatRoom.createMissionChatRoom(savedMission.getId(), List.of(1L, 2L));
        missionChatRoomRepository.save(missionChatRoom);

        // when
        var joinedChatRooms = userMissionChatRoomQueryRepository.findJoinedChatRooms(citizenId);

        // then
        assertThat(joinedChatRooms.get(0).missionId()).isEqualTo(savedMission.getId());
        assertThat(joinedChatRooms.get(0).title()).isEqualTo(savedMission.getMissionInfo().getTitle());
        assertThat(joinedChatRooms.get(0).headCount()).isEqualTo(2);
    }
}