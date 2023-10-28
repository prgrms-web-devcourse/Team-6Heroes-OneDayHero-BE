package com.sixheroes.onedayheroapplication.missionmatch;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.mission.*;
import com.sixheroes.onedayherodomain.mission.repository.MissionCategoryRepository;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
class MissionMatchServiceTest extends IntegrationApplicationTest {

    @Autowired
    private MissionMatchService missionMatchService;

    @Autowired
    private MissionCategoryRepository missionCategoryRepository;

    @Autowired
    private MissionRepository missionRepository;

    @BeforeAll
    public static void setUp(@Autowired MissionCategoryRepository missionCategoryRepository) {
        MissionCategory missionCategory = MissionCategory.from(MissionCategoryCode.MC_001);
        missionCategoryRepository.save(missionCategory);
    }

    @DisplayName("미션이 매칭 중 상태일 때 매칭 완료 상태로 설정할 수 있다.")
    @Test
    void createMissionMatching() {
        // given
        Long citizenId = 1L;
        Long heroId = 2L;
        var mission = createMission(citizenId);

        // when
        var request = createMissionMatchCreateServiceRequest(
                mission,
                heroId
        );
        var response = missionMatchService.createMissionMatch(request);

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.missionId())
                    .isEqualTo(mission.getId());
            soft.assertThat(response.heroId())
                    .isEqualTo(heroId);
            soft.assertThat(mission.getMissionStatus())
                    .isEqualTo(MissionStatus.MATCHING_COMPLETED);
        });
    }

    @DisplayName("미션이 매칭 중 상태가 아니라면 매칭 완료 상태로 설정할 수 없다.")
    @Test
    void createMissionMatchingFail() {
        // given
        Long citizenId = 1L;
        Long heroId = 2L;
        var mission = createMission(citizenId);
        mission.matchingCompleted();

        // when & then
        var request = createMissionMatchCreateServiceRequest(
                mission,
                heroId
        );
        assertThatThrownBy(() -> missionMatchService.createMissionMatch(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.EM_007.name());
    }

    private MissionMatchCreateServiceRequest createMissionMatchCreateServiceRequest(
            Mission mission,
            Long heroId
    ) {
        return MissionMatchCreateServiceRequest.builder()
                .missionId(mission.getId())
                .heroId(heroId)
                .build();
    }

    private Mission createMission(
            Long citizenId
    ) {
        var mission = Mission.builder()
                .missionInfo(createMissionInfo())
                .missionCategory(missionCategoryRepository.findById(1L).get())
                .citizenId(citizenId)
                .regionId(1L)
                .location(new Point(1234, 5678))
                .build();

        return missionRepository.save(mission);
    }

    private MissionInfo createMissionInfo() {
        return MissionInfo.builder()
                .missionDate(LocalDate.of(2023, 10, 10))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(10, 30))
                .deadlineTime(LocalTime.of(10, 0))
                .price(10000)
                .content("서빙 도와주기")
                .build();
    }
}
