package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import com.sixheroes.onedayherochat.application.repository.request.MissionChatRoomRedisRequest;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayherodomain.user.*;
import com.sixheroes.onedayheromongo.chat.ChatMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

@Transactional
class ChatRoomServiceTest extends IntegrationApplicationTest {

    @DisplayName("유저는 채팅방을 생성 할 수 있다.")
    @Test
    void createChatRoom() {
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

    @DisplayName("유저는 동일한 채팅방을 생성 할 수 없다.")
    @Test
    void createChatRoomWithDuplicate() {
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

        chatRoomService.createChatRoom(request);

        // when & then
        assertThatThrownBy(() -> chatRoomService.createChatRoom(request))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("유저가 둘 다 나가있지 않은 상태에서 채팅방을 나갈 수 있다.")
    @Test
    void exitChatRoomWithMaxConnection() {
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

        var chatRoom = chatRoomService.createChatRoom(request);

        // when
        chatRoomService.exitChatRoom(chatRoom.id(), 1L);

        var findMissionChatRoom = missionChatRoomRepository.findById(chatRoom.id()).get();
        var userMissionChatRooms = userMissionChatRoomRepository.findByMissionChatRoom_Id(chatRoom.id());

        // then
        assertThat(findMissionChatRoom.getHeadCount()).isEqualTo(1);
        assertThat(userMissionChatRooms.size()).isEqualTo(1);
    }

    @DisplayName("유저 한명이 나간 상태에서 채팅방을 나가면 채팅방이 삭제 된다.")
    @Test
    void exitChatRoomWithAlone() {
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

        var chatRoom = chatRoomService.createChatRoom(request);
        chatRoomService.exitChatRoom(chatRoom.id(), 1L);

        // when
        chatRoomService.exitChatRoom(chatRoom.id(), 2L);
        var findMissionChatRoom = missionChatRoomRepository.findById(chatRoom.id());

        // then
        assertThat(findMissionChatRoom).isEmpty();
    }

    @DisplayName("현재 유저가 들어가 있는 채팅방 목록을 확인 할 수 있다.")
    @Test
    void findJoinedChatRooms() {
        // given
        var missionId = 1L;

        var userA = User.builder()
                .email(Email.builder()
                        .email("abc@123.com")
                        .build())
                .userBasicInfo(UserBasicInfo.builder()
                        .nickname("시민 A")
                        .birth(LocalDate.of(1990, 1, 1))
                        .gender(UserGender.MALE)
                        .introduce("그냥 한 번 만들어봤습니다.")
                        .build())
                .userFavoriteWorkingDay(UserFavoriteWorkingDay.builder()
                        .favoriteDate(List.of(Week.MON, Week.THU))
                        .favoriteStartTime(LocalTime.of(12, 0, 0))
                        .favoriteEndTime(LocalTime.of(18, 0, 0))
                        .build())
                .userSocialType(UserSocialType.KAKAO)
                .userRole(UserRole.MEMBER)
                .build();


        var userB = User.builder()
                .email(Email.builder()
                        .email("abcde@123.com")
                        .build())
                .userBasicInfo(UserBasicInfo.builder()
                        .nickname("시민 B")
                        .birth(LocalDate.of(1990, 1, 10))
                        .gender(UserGender.MALE)
                        .introduce("설거지 일등 공신")
                        .build())
                .userFavoriteWorkingDay(UserFavoriteWorkingDay.builder()
                        .favoriteDate(List.of(Week.MON, Week.THU))
                        .favoriteStartTime(LocalTime.of(12, 0, 0))
                        .favoriteEndTime(LocalTime.of(18, 0, 0))
                        .build())
                .userSocialType(UserSocialType.KAKAO)
                .userRole(UserRole.MEMBER)
                .build();

        var userC = User.builder()
                .email(Email.builder()
                        .email("abcdefgh@123.com")
                        .build())
                .userBasicInfo(UserBasicInfo.builder()
                        .nickname("시민 C")
                        .birth(LocalDate.of(1990, 1, 10))
                        .gender(UserGender.FEMALE)
                        .introduce("누구보다 벌레 잡기를 잘합니다.")
                        .build())
                .userFavoriteWorkingDay(UserFavoriteWorkingDay.builder()
                        .favoriteDate(List.of(Week.MON, Week.THU))
                        .favoriteStartTime(LocalTime.of(12, 0, 0))
                        .favoriteEndTime(LocalTime.of(18, 0, 0))
                        .build())
                .userSocialType(UserSocialType.KAKAO)
                .userRole(UserRole.MEMBER)
                .build();

        userRepository.saveAll(List.of(userA, userB, userC));

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var mission = Mission.builder()
                .missionCategory(missionCategory)
                .citizenId(1L)
                .regionId(1L)
                .location(Mission.createPoint(127.3324, 36.1807))
                .missionInfo(MissionInfo.builder()
                        .missionDate(LocalDate.of(2023, 11, 25))
                        .startTime(LocalTime.of(9, 0, 0))
                        .endTime(LocalTime.of(14, 0, 0))
                        .deadlineTime(LocalDateTime.of(
                                LocalDate.of(2023, 11, 25),
                                LocalTime.of(9, 0, 0)
                        ))
                        .serverTime(LocalDateTime.of(
                                LocalDate.of(2023, 10, 18),
                                LocalTime.MIDNIGHT
                        ))
                        .title("벌레를 잡아주세요!")
                        .content("이상하게 생긴 벌레가 있어요 ㅠㅠㅠ")
                        .price(5500)
                        .build())
                .bookmarkCount(0)
                .missionStatus(MissionStatus.MATCHING)
                .build();

        missionRepository.save(mission);

        var userIdsA = List.of(1L, 2L);
        var userIdsB = List.of(1L, 3L);

        var requestA = CreateMissionChatRoomServiceRequest.builder()
                .missionId(missionId)
                .userIds(userIdsA)
                .build();

        var requestB = CreateMissionChatRoomServiceRequest.builder()
                .missionId(missionId)
                .userIds(userIdsB)
                .build();

        var missionChatRoomA = MissionChatRoom.createMissionChatRoom(requestA.missionId(), requestA.userIds());
        var redisRequestA = MissionChatRoomRedisRequest.from(missionChatRoomA);

        given(missionChatRoomRedisRepository.create(any(MissionChatRoomRedisRequest.class)))
                .willReturn(redisRequestA);

        var missionChatRoomB = MissionChatRoom.createMissionChatRoom(requestB.missionId(), requestB.userIds());
        var redisRequestB = MissionChatRoomRedisRequest.from(missionChatRoomB);

        given(missionChatRoomRedisRepository.create(any(MissionChatRoomRedisRequest.class)))
                .willReturn(redisRequestB);

        var chatRoomA = chatRoomService.createChatRoom(requestA);
        var chatRoomB = chatRoomService.createChatRoom(requestB);

        var lastChatMessageA = ChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomId(chatRoomA.id())
                .senderId(2L)
                .message("네! 금방 가도록 하겠습니다!")
                .sentMessageTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 27),
                        LocalTime.of(17, 5, 3)))
                .build();

        var lastChatMessageB = ChatMessage.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomId(chatRoomB.id())
                .senderId(3L)
                .message("헉 거리가 생각보다 꽤 되네요.")
                .sentMessageTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 27),
                        LocalTime.of(12, 18, 31)))
                .build();


        var lastChatMessages = List.of(lastChatMessageA, lastChatMessageB);

        given(chatMessageMongoRepository.findLatestMessagesByChatRoomIds(anyList()))
                .willReturn(lastChatMessages);

        // when
        var joinedChatRoom = chatRoomService.findJoinedChatRoom(1L);

        // then
        assertThat(joinedChatRoom)
                .extracting("id", "missionId", "receiverId", "lastSentMessage", "lastSentMessageTime")
                .containsExactlyInAnyOrder(
                        tuple(chatRoomA.id(), chatRoomA.missionId(), 2L, lastChatMessageA.getMessage(), lastChatMessageA.getSentMessageTime()),
                        tuple(chatRoomB.id(), chatRoomB.missionId(), 3L, lastChatMessageB.getMessage(), lastChatMessageB.getSentMessageTime()));
    }
}