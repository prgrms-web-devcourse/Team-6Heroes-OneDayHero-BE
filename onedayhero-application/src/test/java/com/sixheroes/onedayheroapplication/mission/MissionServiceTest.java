package com.sixheroes.onedayheroapplication.mission;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionExtendServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionInfoServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.missionproposal.MissionProposal;
import org.apache.http.entity.ContentType;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@Transactional
class MissionServiceTest extends IntegrationApplicationTest {

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

        var s3ImageUploadServiceRequestA = createS3ImageUploadServiceRequest("image.jpg");
        var s3ImageUploadServiceRequestB = createS3ImageUploadServiceRequest("image.jpg");

        var missionCreateServiceRequest =
                MissionCreateServiceRequest.builder()
                        .missionCategoryId(1L)
                        .regionName("역삼1동")
                        .latitude(123.45)
                        .longitude(123.45)
                        .citizenId(1L)
                        .missionInfo(missionInfoServiceRequest)
                        .imageFiles(List.of(s3ImageUploadServiceRequestA, s3ImageUploadServiceRequestB))
                        .build();

        var S3ImageUploadResponse = List.of(S3ImageUploadServiceResponse.builder()
                        .originalName("image.jpg")
                        .uniqueName("unique123.jpg")
                        .path("s3://path")
                        .build()
                ,
                S3ImageUploadServiceResponse.builder()
                        .originalName("image.jpg")
                        .uniqueName("unique122344.jpg")
                        .path("s3://path1")
                        .build()
        );

        given(s3ImageUploadService.uploadImages(anyList(), any(String.class)))
                .willReturn(S3ImageUploadResponse);

        // when
        var result = missionService.createMission(missionCreateServiceRequest, serverTime);

        // then
        assertThat(result.id()).isNotNull();
    }


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
                .isInstanceOf(BusinessException.class);
    }

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
                .isInstanceOf(BusinessException.class);
    }

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
                .isInstanceOf(BusinessException.class);
    }

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
                .isInstanceOf(BusinessException.class);
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
                .isInstanceOf(BusinessException.class);
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

        var missionUpdateServiceRequest = MissionUpdateServiceRequest.builder()
                .missionCategoryId(updateCategoryId)
                .userId(1L)
                .regionName("역삼1동")
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when
        var result = missionService.updateMission(mission.getId(), missionUpdateServiceRequest, serverTime);

        // then
        assertThat(result.id()).isEqualTo(mission.getId());
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
                .userId(unknownCitizenId)
                .regionName("역삼1동")
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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
                .userId(citizenId)
                .regionName("역삼1동")
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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
                .userId(citizenId)
                .regionName("역삼1동")
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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
                .userId(citizenId)
                .regionName("역삼1동")
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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
                .userId(citizenId)
                .regionName("역삼1동")
                .latitude(1235678.48)
                .longitude(1235678.48)
                .missionInfo(missionInfoServiceRequest)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.updateMission(mission.getId(), missionUpdateServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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

        var missionExtendServiceRequest = MissionExtendServiceRequest.builder()
                .userId(citizenId)
                .missionDate(missionDate.plusDays(2))
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.extendMission(mission.getId(), missionExtendServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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

        var unknownCitizenId = 2L;

        var missionExtendServiceRequest = MissionExtendServiceRequest.builder()
                .userId(unknownCitizenId)
                .missionDate(missionDate.plusDays(2))
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .build();

        // when & then
        assertThatThrownBy(() -> missionService.extendMission(mission.getId(), missionExtendServiceRequest, today))
                .isInstanceOf(BusinessException.class);
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
        assertThat(missionResponse.id()).isEqualTo(mission.getId());
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
                .isInstanceOf(BusinessException.class);
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
        var result = missionService.findOne(citizenId, savedMission.getId());

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
                        "missionStatus",
                        "isBookmarked"
                )
                .containsExactly(
                        MissionCategoryResponse.from(missionCategory),
                        citizenId,
                        RegionResponse.from(region),
                        mission.getLocation().getX(),
                        mission.getLocation().getY(),
                        result.missionInfo(),
                        0,
                        MissionStatus.MATCHING.name(),
                        false
                );
    }

    @DisplayName("시민은 진행중인 미션을 조회 할 수 있다.")
    @Test
    void findProgressMissionByUserId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var region = regionRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);
        var citizenId = 1L;

        var matchingMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING);
        var completedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MISSION_COMPLETED);
        var matchedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING_COMPLETED);
        var expiredMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.EXPIRED);

        var pageRequest = PageRequest.of(0, 3);

        missionRepository.saveAll(List.of(matchingMission, completedMission, matchedMission, expiredMission));

        // when
        var progressMission = missionService.findProgressMissions(pageRequest, citizenId);

        // then
        assertThat(progressMission).hasSize(2);
        assertThat(progressMission.getContent().get(0))
                .extracting(
                        "missionCategory.id",
                        "missionCategory.code",
                        "missionCategory.name",
                        "bookmarkCount",
                        "missionStatus",
                        "imagePath",
                        "isBookmarked"
                ).containsExactly(
                        matchingMission.getMissionCategory().getId(),
                        matchingMission.getMissionCategory().getMissionCategoryCode().name(),
                        matchingMission.getMissionCategory().getMissionCategoryCode().getDescription(),
                        matchingMission.getBookmarkCount(),
                        matchingMission.getMissionStatus().name(),
                        null,
                        false
                );
        assertThat(progressMission.getContent().get(1))
                .extracting(
                        "missionCategory.id",
                        "missionCategory.code",
                        "missionCategory.name",
                        "bookmarkCount",
                        "missionStatus",
                        "imagePath",
                        "isBookmarked"
                ).containsExactly(
                        matchedMission.getMissionCategory().getId(),
                        matchedMission.getMissionCategory().getMissionCategoryCode().name(),
                        matchedMission.getMissionCategory().getMissionCategoryCode().getDescription(),
                        matchedMission.getBookmarkCount(),
                        matchedMission.getMissionStatus().name(),
                        null,
                        false
                );
    }

    @DisplayName("시민은 완료된 미션을 조회 할 수 있다.")
    @Test
    void findCompletedMissionByUserId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var region = regionRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);
        var citizenId = 1L;

        var matchingMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING);
        var completedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MISSION_COMPLETED);
        var matchedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING_COMPLETED);
        var expiredMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.EXPIRED);

        var pageRequest = PageRequest.of(0, 3);

        missionRepository.saveAll(List.of(matchingMission, completedMission, matchedMission, expiredMission));

        // when
        var findCompletedMission = missionService.findCompletedMissionsByUserId(pageRequest, citizenId);

        // then
        assertThat(findCompletedMission).hasSize(1);
        assertThat(findCompletedMission.getContent().get(0))
                .extracting(
                        "missionCategory.id",
                        "missionCategory.code",
                        "missionCategory.name",
                        "bookmarkCount",
                        "missionStatus",
                        "imagePath",
                        "isBookmarked"
                ).containsExactly(
                        completedMission.getMissionCategory().getId(),
                        completedMission.getMissionCategory().getMissionCategoryCode().name(),
                        completedMission.getMissionCategory().getMissionCategoryCode().getDescription(),
                        completedMission.getBookmarkCount(),
                        completedMission.getMissionStatus().name(),
                        null,
                        false
                );
    }

    @DisplayName("시민은 제안할 미션, 즉 매칭 중인 미션을 조회할 수 있다.")
    @Test
    void findMatchingMissionByUserId() {
        // given
        var missionCategory = missionCategoryRepository.findById(1L).get();
        var region = regionRepository.findById(1L).get();

        var serverTime = LocalDateTime.of(LocalDate.of(2023, 10, 9), LocalTime.MIDNIGHT);

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(missionDate, startTime);

        var missionInfo = createMissionInfo(missionDate, startTime, endTime, deadlineTime, serverTime);
        var citizenId = 1L;

        var matchingMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING);
        var matchingMission2 = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING);
        var matchingMission3 = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MATCHING);
        var completedMission = createMission(citizenId, missionCategory, missionInfo, region.getId(), MissionStatus.MISSION_COMPLETED);

        missionRepository.saveAll(List.of(matchingMission, matchingMission2, matchingMission3, completedMission));

        var heroId = 1L;
        var missionProposal = MissionProposal.builder()
            .missionId(matchingMission3.getId())
            .heroId(heroId)
            .build();
        missionProposalRepository.save(missionProposal);

        // when
        var findMatchingMission = missionService.findMatchingMissionsByUserId(citizenId, heroId);

        // then
        var missionMatchingResponses = findMatchingMission.missionMatchingResponses();
        assertThat(missionMatchingResponses)
                .hasSize(2)
                .filteredOn("id", matchingMission.getId())
                .extracting(
                        "title",
                        "missionCategory.id",
                        "missionCategory.code",
                        "missionCategory.name",
                        "region.id",
                        "region.si",
                        "region.gu",
                        "region.dong",
                        "missionDate",
                        "startTime",
                        "endTime",
                        "price",
                        "bookmarkCount",
                        "missionStatus",
                        "imagePath",
                        "isBookmarked"
                ).containsExactly(Tuple.tuple(
                        matchingMission.getMissionInfo().getTitle(),
                        matchingMission.getMissionCategory().getId(),
                        matchingMission.getMissionCategory().getMissionCategoryCode().name(),
                        matchingMission.getMissionCategory().getMissionCategoryCode().getDescription(),
                        region.getId(),
                        region.getSi(),
                        region.getGu(),
                        region.getDong(),
                        matchingMission.getMissionInfo().getMissionDate(),
                        matchingMission.getMissionInfo().getStartTime(),
                        matchingMission.getMissionInfo().getEndTime(),
                        matchingMission.getMissionInfo().getPrice(),
                        matchingMission.getBookmarkCount(),
                        matchingMission.getMissionStatus().name(),
                        null,
                        false
                ));
    }

    private MissionCreateServiceRequest createMissionCreateServiceRequest(
            MissionInfoServiceRequest missionInfoServiceRequest
    ) {
        return MissionCreateServiceRequest.builder()
                .missionCategoryId(1L)
                .citizenId(1L)
                .regionName("역삼1동")
                .latitude(1234252.23)
                .longitude(1234277.388)
                .missionInfo(missionInfoServiceRequest)
                .imageFiles(Collections.emptyList())
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

    private S3ImageUploadServiceRequest createS3ImageUploadServiceRequest(String imageName) {
        return S3ImageUploadServiceRequest.builder()
                .originalName(imageName)
                .contentType(ContentType.MULTIPART_FORM_DATA.toString())
                .inputStream(InputStream.nullInputStream())
                .contentSize(Long.MAX_VALUE)
                .build();
    }

    private Mission createMission(
            Long citizenId,
            MissionCategory missionCategory,
            MissionInfo missionInfo,
            Long regionId,
            MissionStatus missionStatus
    ) {
        return Mission.builder()
                .missionCategory(missionCategory)
                .missionInfo(missionInfo)
                .regionId(regionId)
                .citizenId(citizenId)
                .location(Mission.createPoint(123456.78, 123456.78))
                .missionStatus(missionStatus)
                .bookmarkCount(0)
                .build();
    }

    private MissionInfo createMissionInfo(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime,
            LocalDateTime serverTime
    ) {
        return MissionInfo.builder()
                .title("제목")
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
                .serverTime(serverTime)
                .build();
    }
}