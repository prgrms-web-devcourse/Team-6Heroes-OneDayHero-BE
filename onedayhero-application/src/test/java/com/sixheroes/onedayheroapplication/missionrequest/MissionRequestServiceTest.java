package com.sixheroes.onedayheroapplication.missionrequest;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestApproveServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestRejectServiceRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionCategory;
import com.sixheroes.onedayherodomain.mission.MissionCategoryCode;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.missionrequest.MissionRequest;
import com.sixheroes.onedayherodomain.missionrequest.repository.MissionRequestRepository;
import com.sixheroes.onedayherodomain.user.Email;
import com.sixheroes.onedayherodomain.user.User;
import com.sixheroes.onedayherodomain.user.UserBasicInfo;
import com.sixheroes.onedayherodomain.user.UserFavoriteWorkingDay;
import com.sixheroes.onedayherodomain.user.UserGender;
import com.sixheroes.onedayherodomain.user.UserRole;
import com.sixheroes.onedayherodomain.user.UserSocialType;
import com.sixheroes.onedayherodomain.user.Week;
import com.sixheroes.onedayherodomain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionRequestServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionRequestService missionRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionRequestRepository missionRequestRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @DisplayName("미션 제안을 생성한다.")
    @Transactional
    @Test
    void createMissionRequest() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var hero = userRepository.save(createUser());
        hero.changeHeroModeOn();

        var missionRequestCreateServiceRequest = new MissionRequestCreateServiceRequest(
            citizenId,
            mission.getId(),
            hero.getId()
        );

        // when
        var missionRequestCreateResponse = missionRequestService.createMissionRequest(missionRequestCreateServiceRequest);

        // then
        var missionRequestId = missionRequestCreateResponse.missionRequestId();
        var missionRequest = missionRequestRepository.findById(missionRequestId);
        assertThat(missionRequest).isNotEmpty();
        assertThat(missionRequestCreateResponse.missionId()).isEqualTo(mission.getId());
        assertThat(missionRequestCreateResponse.heroId()).isEqualTo(hero.getId());
        assertThat(missionRequestCreateResponse.missionRequestStatus()).isEqualTo("REQUEST");
    }

    @DisplayName("미션 제안을 생성할 때 해당 미션이 존재하지 않으면 예외가 발생한다.")
    @Transactional
    @Test
    void createMissionRequestNotExistMission() {
        // given
        var citizenId = 1L;
        var missionId = 1L;

        var hero = userRepository.save(createUser());
        hero.changeHeroModeOn();

        var missionRequestCreateServiceRequest = new MissionRequestCreateServiceRequest(
            citizenId,
            missionId,
            hero.getId()
        );

        // when & then
        assertThatThrownBy(() -> missionRequestService.createMissionRequest(missionRequestCreateServiceRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EMC_000.name());
    }

    @DisplayName("미션 제안을 생성할 때 해당 히어로가 존재하지 않으면 예외가 발생한다.")
    @Transactional
    @Test
    void createMissionRequestNotExistHero() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var hero = userRepository.save(createUser());
        hero.changeHeroModeOn();

        var missionRequestCreateServiceRequest = new MissionRequestCreateServiceRequest(
            citizenId,
            mission.getId(),
            4L
        );

        // when & then
        assertThatThrownBy(() -> missionRequestService.createMissionRequest(missionRequestCreateServiceRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EUC_000.name());
    }

    @DisplayName("미션 제안을 승낙한다.")
    @Transactional
    @Test
    void approveMissionRequst() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var heroId = 1L;
        var missionRequest = missionRequestRepository.save(createMissionRequest(mission.getId(), heroId));

        var missionRequestApproveServiceRequest = new MissionRequestApproveServiceRequest(heroId);

        // when
        var missionRequestApproveResponse = missionRequestService.approveMissionRequest(
            missionRequest.getId(),
            missionRequestApproveServiceRequest
        );

        // then
        assertThat(missionRequestApproveResponse.missionRequestId()).isEqualTo(missionRequest.getId());
        assertThat(missionRequestApproveResponse.missionId()).isEqualTo(missionRequest.getMissionId());
        assertThat(missionRequestApproveResponse.heroId()).isEqualTo(missionRequest.getId());
        assertThat(missionRequestApproveResponse.missionRequestStatus()).isEqualTo("APPROVE");
    }

    @DisplayName("미션 제안이 존재하지 않으면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Transactional
    @Test
    void doNotApproveMissionRequstWhenNotExsistRequest() {
        // given
        var heroId = 1L;
        var missionRequestId = 1L;

        var missionRequestApproveServiceRequest = new MissionRequestApproveServiceRequest(heroId);

        // when
        assertThatThrownBy(() -> missionRequestService.approveMissionRequest(missionRequestId, missionRequestApproveServiceRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EMR_000.name());
    }

    @DisplayName("미션이 존재하지 않으면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Transactional
    @Test
    void doNotApproveMissionRequstWhenNotExsistMission() {
        // given
        var missionId = 1L;
        var heroId = 1L;
        var missionRequest = missionRequestRepository.save(createMissionRequest(missionId, heroId));


        var missionRequestApproveServiceRequest = new MissionRequestApproveServiceRequest(heroId);

        // when
        assertThatThrownBy(() -> missionRequestService.approveMissionRequest(missionRequest.getId(), missionRequestApproveServiceRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EMC_000.name());
    }

    @DisplayName("미션 제안을 거절한다.")
    @Transactional
    @Test
    void rejectMissionRequst() {
        // given
        var citizenId = 1L;
        var missionCategory = missionCategoryRepository.save(createMissionCategory());
        var mission = missionRepository.save(createMission(citizenId, missionCategory));

        var heroId = 1L;
        var missionRequest = missionRequestRepository.save(createMissionRequest(mission.getId(), heroId));

        var missionRequestRejectServiceRequest = new MissionRequestRejectServiceRequest(heroId);

        // when
        var missionRequestRejectResponse = missionRequestService.rejectMissionRequest(
            missionRequest.getId(),
            missionRequestRejectServiceRequest
        );

        // then
        assertThat(missionRequestRejectResponse.missionRequestId()).isEqualTo(missionRequest.getId());
        assertThat(missionRequestRejectResponse.missionId()).isEqualTo(missionRequest.getMissionId());
        assertThat(missionRequestRejectResponse.heroId()).isEqualTo(missionRequest.getHeroId());
        assertThat(missionRequestRejectResponse.missionRequestStatus()).isEqualTo("REJECT");
    }

    @DisplayName("미션 제안이 존재하지 않으면 미션 제안을 거절할 때 예외가 발생한다.")
    @Test
    void doNotRejectMissionRequstWhenNotExsistRequest() {
        // given
        var heroId = 1L;
        var missionRequestId = 1L;

        var missionRequestRejectServiceRequest = new MissionRequestRejectServiceRequest(heroId);

        // when
        assertThatThrownBy(() -> missionRequestService.rejectMissionRequest(missionRequestId, missionRequestRejectServiceRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EMR_000.name());
    }

    @DisplayName("미션이 존재하지 않으면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Test
    void doNotRejectMissionRequstWhenNotExsistMission() {
        // given
        var missionId = 1L;
        var heroId = 1L;
        var missionRequest = missionRequestRepository.save(createMissionRequest(missionId, heroId));


        var missionRequestRejectServiceRequest = new MissionRequestRejectServiceRequest(heroId);

        // when
        assertThatThrownBy(() -> missionRequestService.rejectMissionRequest(missionRequest.getId(), missionRequestRejectServiceRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage(ErrorCode.EMC_000.name());
    }

    private MissionRequest createMissionRequest(
        Long missionId,
        Long heroId
    ) {
        return MissionRequest.builder()
            .missionId(missionId)
            .heroId(heroId)
            .build();
    }

    private Mission createMission(
        Long citizenId,
        MissionCategory missionCategory
    ) {
        return Mission.builder()
            .regionId(1L)
            .citizenId(citizenId)
            .missionCategory(missionCategory)
            .missionInfo(createMissionInfo())
            .location(new Point(123.123, 123.123))
            .missionInfo(createMissionInfo())
            .build();
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
            .content("미션 내용입니다.")
            .missionDate(LocalDate.of(2023, 10, 10))
            .startTime(LocalTime.of(10, 0, 0))
            .endTime(LocalTime.of(10, 30, 0))
            .deadlineTime(LocalTime.of(10, 0, 0))
            .price(1000)
            .build();
    }

    private MissionCategory createMissionCategory() {
        return MissionCategory.from(MissionCategoryCode.MC_001);
    }

    private User createUser() {
        return User.builder()
            .userBasicInfo(createUserBasicInfo())
            .userFavoriteWorkingDay(createUserFavoriteWorkingDay())
            .userSocialType(UserSocialType.KAKAO)
            .userRole(UserRole.MEMBER)
            .email(createEmail())
            .build();
    }

    private UserBasicInfo createUserBasicInfo() {
        return UserBasicInfo.builder()
            .nickname("이름")
            .birth(LocalDate.of(1990, 1, 1))
            .gender(UserGender.MALE)
            .introduce("자기소개")
            .build();
    }

    private UserFavoriteWorkingDay createUserFavoriteWorkingDay() {
        return UserFavoriteWorkingDay.builder()
            .favoriteDate(List.of(Week.MON, Week.THU))
            .favoriteStartTime(LocalTime.of(12, 0, 0))
            .favoriteEndTime(LocalTime.of(18, 0, 0))
            .build();
    }

    private Email createEmail() {
        return Email.builder()
            .email("abc@123.com")
            .build();
    }
}