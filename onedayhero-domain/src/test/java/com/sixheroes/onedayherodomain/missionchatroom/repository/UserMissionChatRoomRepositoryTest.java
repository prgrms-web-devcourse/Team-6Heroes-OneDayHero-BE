package com.sixheroes.onedayherodomain.missionchatroom.repository;

import com.sixheroes.onedayherodomain.IntegrationRepositoryTest;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayherodomain.user.*;
import com.sixheroes.onedayherodomain.user.repository.UserImageRepository;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
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
class UserMissionChatRoomRepositoryTest extends IntegrationRepositoryTest {

    @Autowired
    private MissionChatRoomRepository missionChatRoomRepository;

    @Autowired
    private UserMissionChatRoomRepository userMissionChatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @DisplayName("유저가 접속중인 채팅방의 정보를 확인 할 수 있다.")
    @Test
    void findChatRoomInfoByUserId() {
        // given
        var userA = createUser("닉네임1");
        var userB = createUser("닉네임2");
        var savedUserA = userRepository.save(userA);
        var savedUserB = userRepository.save(userB);

        var userImageA = createUserImage(savedUserA);
        var savedUserImageA = userImageRepository.save(userImageA);

        var userImageB = createUserImage(savedUserB);
        var savedUserImageB = userImageRepository.save(userImageB);

        var missionCategory = createMissionCategory();
        missionCategoryRepository.save(missionCategory);

        var mission = createMission(missionCategory, savedUserA);
        var savedMission = missionRepository.save(mission);

        var missionChatRoom = MissionChatRoom.createMissionChatRoom(savedMission.getId(), List.of(userA.getId(), userB.getId()));
        var savedMissionChatroom = missionChatRoomRepository.save(missionChatRoom);

        // when
        var receiverChatRoomInfo = userMissionChatRoomRepository.findReceiverChatRoomInfoInChatRoomIdsAndUserId(List.of(savedMissionChatroom.getId()), userB.getId());

        // then
        assertThat(receiverChatRoomInfo).isNotEmpty();
        assertThat(receiverChatRoomInfo.get(0).chatRoomId()).isEqualTo(savedMissionChatroom.getId());
        assertThat(receiverChatRoomInfo.get(0).missionId()).isEqualTo(savedMission.getId());
        assertThat(receiverChatRoomInfo.get(0).receiverId()).isEqualTo(savedUserA.getId());
        assertThat(receiverChatRoomInfo.get(0).path()).isEqualTo(savedUserImageA.getPath());
        assertThat(receiverChatRoomInfo.get(0).title()).isEqualTo(savedMission.getMissionInfo().getTitle());
        assertThat(receiverChatRoomInfo.get(0).headCount()).isEqualTo(savedMissionChatroom.getHeadCount());
    }

    private MissionCategory createMissionCategory() {
        return MissionCategory.builder()
                .id(MissionCategoryCode.MC_001.getCategoryId())
                .missionCategoryCode(MissionCategoryCode.MC_001)
                .name("서빙")
                .build();
    }

    private Mission createMission(MissionCategory missionCategory, User savedUser) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .missionInfo(
                        MissionInfo.builder()
                                .title("title")
                                .content("content")
                                .missionDate(LocalDate.of(2023, 11, 1))
                                .startTime(LocalTime.of(12, 30))
                                .endTime(LocalTime.of(14, 30))
                                .deadlineTime(LocalDateTime.of(
                                        LocalDate.of(2023, 11, 1),
                                        LocalTime.of(12, 0)
                                ))
                                .price(1000)
                                .serverTime(LocalDateTime.of(
                                        LocalDate.of(2023, 10, 31),
                                        LocalTime.MIDNIGHT
                                ))
                                .build())
                .regionId(1L)
                .citizenId(savedUser.getId())
                .location(Mission.createPoint(123456.78, 123456.78))
                .missionStatus(MissionStatus.MATCHING)
                .bookmarkCount(0)
                .build();
    }

    private UserImage createUserImage(User savedUser) {
        return UserImage.createUserImage(
                savedUser,
                "원본 이름",
                "고유 이름",
                "s3://path"
        );
    }

    private User createUser(String nickName) {
        return User.builder()
                .userBasicInfo(UserBasicInfo.builder()
                        .nickname(nickName)
                        .birth(LocalDate.of(1990, 1, 1))
                        .gender(UserGender.MALE)
                        .introduce("자기소개")
                        .build())
                .userFavoriteWorkingDay(UserFavoriteWorkingDay.builder()
                        .favoriteDate(List.of(Week.MON, Week.THU))
                        .favoriteStartTime(LocalTime.of(12, 0, 0))
                        .favoriteEndTime(LocalTime.of(18, 0, 0))
                        .build())
                .userSocialType(UserSocialType.KAKAO)
                .userRole(UserRole.MEMBER)
                .email(Email.builder()
                        .email("abc@123.com")
                        .build())
                .build();
    }
}