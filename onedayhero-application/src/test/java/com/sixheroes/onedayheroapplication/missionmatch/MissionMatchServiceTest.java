package com.sixheroes.onedayheroapplication.missionmatch;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCancelServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionInfo;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.missionmatch.MissionMatchStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
class MissionMatchServiceTest extends IntegrationApplicationTest {

    @DisplayName("시민은 본인이 작성한 미션이 매칭 중 상태일 때, 매칭완료 상태를 가지는 미션매칭을 생성할 수 있다.")
    @Test
    void createMissionMatching() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMission(citizenId);

        // when
        var request = createMissionMatchCreateServiceRequest(
                citizenId,
                mission,
                heroId
        );
        var response = missionMatchService.createMissionMatch(
                request
        );
        var createdMissionMatching = missionMatchReader.findByMissionId(mission.getId());

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.missionId())
                    .isEqualTo(mission.getId());
            soft.assertThat(response.heroId())
                    .isEqualTo(heroId);
            soft.assertThat(mission.getMissionStatus())
                    .isEqualTo(MissionStatus.MATCHING_COMPLETED);
            soft.assertThat(createdMissionMatching.getMissionMatchStatus())
                    .isEqualTo(MissionMatchStatus.MATCHED);
        });
    }

    @DisplayName("시민은 미션이 매칭 중 상태가 아니라면, 매칭완료 상태를 가지는 미션매칭을 생성할 수 없다.")
    @EnumSource(value = MissionStatus.class, mode = EnumSource.Mode.EXCLUDE, names = "MATCHING")
    @ParameterizedTest
    void createMissionMatchingFailWithInvalidStatus(MissionStatus missionStatus) {
        System.out.println(missionStatus);
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMissionWithStatus(
                citizenId, missionStatus
        );

        // when & then
        var request = createMissionMatchCreateServiceRequest(
                citizenId,
                mission,
                heroId
        );
        assertThatThrownBy(() -> missionMatchService.createMissionMatch(request)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_007.name());
    }

    @DisplayName("시민은 본인이 작성한 미션이 아니라면, 매칭완료 상태를 가지는 미션매칭을 생성할 수 없다.")
    @Test
    void createMissionMatchingFailWithInvalidOwn() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var otherUserId = 3L;
        var mission = createMission(citizenId);

        // when & then
        var request = createMissionMatchCreateServiceRequest(
                otherUserId,
                mission,
                heroId
        );
        assertThatThrownBy(() -> missionMatchService.createMissionMatch(request)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_008.name());
    }

    @DisplayName("시민은 본인이 작성한 미션이 매칭 됨 상태일 떄, 연결된 미션매칭에 대해 취소 상태로 변경할 수 있다.")
    @Test
    void cancelMissionMatch() {
        // given
        var citizenId = 1L;
        var heroId = 2L;
        var mission = createMission(citizenId);
        missionMatchService.createMissionMatch(
                createMissionMatchCreateServiceRequest(
                        citizenId,
                        mission,
                        heroId
                )
        );

        // when
        var request = createMissionMatchCancelServiceRequest(
                citizenId,
                mission.getId()
        );
        var response = missionMatchService.cancelMissionMatch(request);
        var canceledMissionMatching = missionMatchReader.findByMissionId(mission.getId());

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.missionId())
                    .isEqualTo(mission.getId());
            soft.assertThat(response.citizenId())
                    .isEqualTo(citizenId);
            soft.assertThat(canceledMissionMatching.getMissionMatchStatus())
                    .isEqualTo(MissionMatchStatus.CANCELED);
        });
    }

    private MissionMatchCreateServiceRequest createMissionMatchCreateServiceRequest(
            Long userId,
            Mission mission,
            Long heroId
    ) {
        return MissionMatchCreateServiceRequest.builder()
                .userId(userId)
                .missionId(mission.getId())
                .heroId(heroId)
                .build();
    }

    private MissionMatchCancelServiceRequest createMissionMatchCancelServiceRequest(
            Long citizenId,
            Long missionId
    ) {
        return MissionMatchCancelServiceRequest.builder()
                .citizenId(citizenId)
                .missionId(missionId)
                .build();
    }

    private Mission createMission(
            Long citizenId
    ) {
        var mission = Mission.builder()
                .missionInfo(createMissionInfo())
                .missionCategory(missionCategoryRepository.findById(1L).get())
                .bookmarkCount(0)
                .missionStatus(MissionStatus.MATCHING)
                .citizenId(citizenId)
                .regionId(1L)
                .location(Mission.createPoint(1234.56, 1234.78))
                .build();

        return missionRepository.save(mission);
    }

    private Mission createMissionWithStatus(
            Long citizenId,
            MissionStatus missionStatus
    ) {
        var mission = Mission.builder()
                .missionInfo(createMissionInfo())
                .missionCategory(missionCategoryRepository.findById(1L).get())
                .bookmarkCount(0)
                .missionStatus(missionStatus)
                .citizenId(citizenId)
                .regionId(1L)
                .location(Mission.createPoint(1234.56, 1234.56))
                .build();

        return missionRepository.save(mission);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 10, 10),
                        LocalTime.of(10, 0)
                ))
                .price(10000)
                .title("서빙")
                .content("서빙 도와주기")
                .serverTime(
                        LocalDateTime.of(
                                LocalDate.of(2023, 10, 9),
                                LocalTime.MIDNIGHT
                        )
                )
                .build();
    }
}
