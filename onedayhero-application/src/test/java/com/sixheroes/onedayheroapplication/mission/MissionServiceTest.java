package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionInfoServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionBookmarkRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.region.repository.RegionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionBookmarkRepository missionBookmarkRepository;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MissionService missionService;

    @Transactional
    @DisplayName("시민은 미션을 생성 할 수 있다.")
    @Test
    void createMission() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        var missionCategoryId = missionCreateServiceRequest.missionCategoryId();
        var missionCategory = missionCategoryRepository.findById(missionCategoryId).get();
        var region = regionRepository.findById(1L).get();


        // when
        var result = missionService.createMission(missionCreateServiceRequest, serverTime);

        // then
        assertThat(result)
                .extracting(
                        "missionCategory",
                        "citizenId",
                        "region",
                        "longitude",
                        "latitude",
                        "missionInfo",
                        "bookmarkCount",
                        "missionStatus"
                )
                .containsExactly(
                        MissionCategoryResponse.from(missionCategory),
                        missionCreateServiceRequest.citizenId(),
                        RegionResponse.from(region),
                        missionCreateServiceRequest.longitude(),
                        missionCreateServiceRequest.latitude(),
                        result.missionInfo(),
                        0,
                        MissionStatus.MATCHING.name()
                );
    }

    @Transactional
    @DisplayName("시민이 미션을 생성 할 때 미션의 수행 날짜가 생성 날짜보다 이전 일 수 없다.")
    @Test
    void createMissionWithMissionDateBeforeToday() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 21, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when & then
        assertThatThrownBy(() -> missionService.createMission(missionCreateServiceRequest, serverTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_003.name());
    }

    @Transactional
    @DisplayName("시민이 미션을 생성 할 때 미션의 종료 시간이 시작 시간 이전 일 수 없다.")
    @Test
    void createMissionWithEndTimeBeforeStartTime() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(9, 30, 0);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when & then
        assertThatThrownBy(() -> missionService.createMission(missionCreateServiceRequest, serverTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_004.name());
    }

    @Transactional
    @DisplayName("시민이 미션을 생성 할 때 미션의 마감 시간이 시작 시간 이후 일 수 없다.")
    @Test
    void createMissionWithDeadLineTimeAfterStartTime() {
        // given
        var serverTime = LocalDateTime.of(2023, 10, 20, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 20);
        var startTime = LocalTime.of(10, 0, 0);
        var endTime = LocalTime.of(10, 30, 0);
        var deadlineTime = LocalDateTime.of(
                missionDate,
                startTime.plusMinutes(1)
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateServiceRequest = createMissionCreateServiceRequest(missionInfoServiceRequest);

        // when & then
        assertThatThrownBy(() -> missionService.createMission(missionCreateServiceRequest, serverTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_005.name());
    }

    @Transactional
    @DisplayName("시민은 미션을 삭제 할 수 있다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "MATCHING_COMPLETED")
    @ParameterizedTest
    void deleteMission(MissionStatus missionStatus) {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                missionStatus
        );

        var savedMission = missionRepository.save(mission);

        missionBookmarkRepository.saveAll(
                List.of(
                        createMissionBookmark(citizenId, savedMission),
                        createMissionBookmark(2L, savedMission)
                )
        );


        // when
        missionService.deleteMission(savedMission.getId(), citizenId);

        var findMission = missionRepository.findById(savedMission.getId());
        var findUserBookMarkMission = missionBookmarkRepository.findByMissionId(savedMission.getId());

        // then
        assertThat(findMission).isEmpty();
        assertThat(findUserBookMarkMission).isEmpty();
    }

    @DisplayName("미션 매칭이 완료 된 상태에서는 미션을 삭제 할 수 없다.")
    @Test
    void deleteMissionWithMatchingCompleteStatus() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING_COMPLETED
        );

        var savedMission = missionRepository.save(mission);

        // when & then
        assertThatThrownBy(() -> missionService.deleteMission(savedMission.getId(), citizenId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_007.name());
    }

    @DisplayName("미션을 생성한 시민이 아니라면 삭제를 할 수 없다.")
    @Test
    void deleteMissionWithInValidUser() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);

        var unknownCitizenId = 2L;

        // when & then
        assertThatThrownBy(() -> missionService.deleteMission(savedMission.getId(), unknownCitizenId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_100.name());
    }

    @DisplayName("시민은 미션을 수정 할 수 있다.")
    @Test
    void updateMission() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var region = regionRepository.findById(1L).get();

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(3),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(2)
        );

        var updateCategoryId = 2L;
        var updateMissionCategory = missionCategoryRepository.findById(updateCategoryId).get();

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(1L)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when
        var result = missionService.updateMission(mission.getId(), missionUpdateServiceRequest, serverTime);

        // then
        assertThat(result)
                .extracting(
                        "missionCategory",
                        "citizenId",
                        "region",
                        "longitude",
                        "latitude",
                        "missionInfo",
                        "bookmarkCount",
                        "missionStatus"
                )
                .containsExactly(
                        MissionCategoryResponse.from(updateMissionCategory),
                        missionUpdateServiceRequest.citizenId(),
                        RegionResponse.from(region),
                        missionUpdateServiceRequest.longitude(),
                        missionUpdateServiceRequest.latitude(),
                        result.missionInfo(),
                        0,
                        MissionStatus.MATCHING.name()
                );
    }

    @DisplayName("시민은 본인이 만든 미션만 수정 할 수 있다.")
    @Test
    void updateMissionWithInvalidUser() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);
        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(3),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(1)
        );

        var updateCategoryId = 2L;
        var unknownCitizenId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(unknownCitizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_100.name());
    }

    @DisplayName("시민은 매칭 중인 상태의 미션만 수정이 가능하다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "MATCHING")
    @ParameterizedTest
    void updateMissionWithStatusIsMatching(MissionStatus missionStatus) {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                missionStatus
        );

        var savedMission = missionRepository.save(mission);
        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(3),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(1)
        );

        var updateCategoryId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(citizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_009.name());
    }

    @DisplayName("시민이 미션을 수정 할 때 미션 일이 현재보다 이전 일 수 없다.")
    @Test
    void updateMissionWithMissionDateBeforeToday() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);

        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                today.minusDays(1L).toLocalDate(),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(3),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(1)
        );

        var updateCategoryId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(citizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_003.name());
    }

    @DisplayName("시민이 미션을 수정 할 때 미션의 마감 일이 시작 시간 이후 일 수 없다.")
    @Test
    void updateMissionWithDeadlineTimeAfterStartTime() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);

        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(1),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(1)
        );

        var updateCategoryId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(citizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_004.name());
    }

    @DisplayName("시민이 미션을 수정 할 때 미션의 종료 시간이 시작 시간보다 빠를 수 없다.")
    @Test
    void updateMissionWithEndTimeBeforeStartTime() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);

        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(3),
                savedMission.getMissionInfo().getEndTime().plusHours(2),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(3)
        );

        var updateCategoryId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(citizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.EM_004.name());
    }

    @DisplayName("시민은 마감된 미션에 대해서만 미션을 연장 할 수 있다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "EXPIRED")
    @ParameterizedTest
    void extendMissionWithStatusIsExpired(MissionStatus missionStatus) {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                missionStatus
        );

        var savedMission = missionRepository.save(mission);
        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(3),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(1)
        );

        var updateCategoryId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(citizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.extendMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.T_001.name());
    }

    @DisplayName("시민은 본인이 만든 미션만 연장 할 수 있다.")
    @Test
    void extendMissionWithInvalidUser() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);
        var today = LocalDateTime.of(
                savedMission.getMissionInfo().getMissionDate().minusDays(1),
                LocalTime.MIDNIGHT
        );

        var missionInfoServiceRequest = createMissionInfoServiceRequest(
                savedMission.getMissionInfo().getMissionDate().plusDays(2),
                savedMission.getMissionInfo().getStartTime().plusHours(2),
                savedMission.getMissionInfo().getEndTime().plusHours(3),
                savedMission.getMissionInfo().getDeadlineTime().plusHours(1)
        );

        var updateCategoryId = 2L;
        var unknownCitizenId = 2L;

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .citizenId(unknownCitizenId)
                .regionId(1L)
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.extendMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_100.name());
    }

    @DisplayName("시민은 미션을 완료 상태로 변경 할 수 있다.")
    @Test
    void completeMission() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING_COMPLETED
        );
        var savedMission = missionRepository.save(mission);

        // when
        var missionResponse = missionService.completeMission(savedMission.getId(), citizenId);

        // then
        assertThat(missionResponse.missionStatus()).isEqualTo(MissionStatus.MISSION_COMPLETED.name());
    }

    @DisplayName("시민은 본인이 만든 미션이 아니면 미션을 완료 상태로 변경 할 수 없다.")
    @Test
    void completeMissionWithUnknownCitizen() {
        // given
        var citizenId = 1L;
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionCategory = missionCategoryRepository.findById(1L).get();

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING_COMPLETED
        );
        var savedMission = missionRepository.save(mission);
        var unknownCitizenId = 2L;

        // when & then
        assertThatThrownBy(() -> savedMission.complete(unknownCitizenId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_100.name());
    }

    @DisplayName("시민은 하나의 미션을 조회 할 수 있다.")
    @Test
    void findOneMission() {
        // given
        var citizenId = 1L;
        var region = regionRepository.findById(1L).get();
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var serverTime = LocalDateTime.of(2023, 10, 10, 0, 0);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var mission = createMission(
                missionCategory,
                citizenId,
                missionDate,
                startTime,
                endTime,
                deadlineTime,
                serverTime,
                MissionStatus.MATCHING
        );

        var savedMission = missionRepository.save(mission);

        // when
        var result = missionService.findOne(savedMission.getId());

        // then
        assertThat(result)
                .extracting(
                        "missionCategory",
                        "citizenId",
                        "region",
                        "longitude",
                        "latitude",
                        "missionInfo",
                        "bookmarkCount",
                        "missionStatus"
                )
                .containsExactly(
                        MissionCategoryResponse.from(missionCategory),
                        citizenId,
                        RegionResponse.from(region),
                        mission.getLocation().getX(),
                        mission.getLocation().getY(),
                        result.missionInfo(),
                        0,
                        MissionStatus.MATCHING.name()
                );
    }

    private MissionCreateServiceRequest createMissionCreateServiceRequest(
            MissionInfoServiceRequest missionInfoServiceRequest
    ) {
        return MissionCreateServiceRequest.builder()
                .missionCategoryId(1L)
                .citizenId(1L)
                .regionId(1L)
                .latitude(1234252.23)
                .longitude(1234277.388)
                .missionInfo(missionInfoServiceRequest)
                .build();
    }

    private MissionInfoServiceRequest createMissionInfoServiceRequest(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime
    ) {
        return MissionInfoServiceRequest
                .builder()
                .title("제목")
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
                .build();
    }

    private Mission createMission(
            MissionCategory missionCategory,
            Long citizenId,
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            LocalDateTime serverTime,
            MissionStatus missionStatus
    ) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .missionInfo(
                        MissionInfo.builder()
                                .title("title")
                                .content("content")
                                .missionDate(missionDate)
                                .startTime(startTime)
                                .endTime(endTime)
                                .deadlineTime(deadlineTime)
                                .price(1000)
                                .serverTime(serverTime)
                                .build())
                .regionId(1L)
                .citizenId(citizenId)
                .location(Mission.createPoint(123456.78, 123456.89))
                .bookmarkCount(0)
                .missionStatus(missionStatus)
                .build();
    }

    private MissionBookmark createMissionBookmark(
            Long citizenId,
            Mission mission
    ) {
        return MissionBookmark.builder()
                .userId(citizenId)
                .mission(mission)
                .build();
    }
}