package com.sixheroes.onedayheroapplication.main;

import com.sixheroes.onedayheroapplication.IntegrationApplicationTest;
import com.sixheroes.onedayheroapplication.main.request.UserPositionServiceRequest;
import com.sixheroes.onedayheroapplication.main.response.MissionSoonExpiredResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayherodomain.mission.Mission;
import com.sixheroes.onedayherodomain.mission.MissionStatus;
import com.sixheroes.onedayherodomain.mission.repository.MissionRepository;
import com.sixheroes.onedayherodomain.mission.repository.response.MainMissionQueryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


public class MainServiceTest extends IntegrationApplicationTest {

    @MockBean
    protected MissionRepository missionNativeQueryRepository;

    @DisplayName("메인 페이지를 조회 할 수 있다.")
    @Test
    void findMainResponse() {
        // given
        var latitude = 37.37898914571587;
        var longitude = 121.66272838210327;
        var userPosition = UserPositionServiceRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
        var userId = 1L;
        var serverTime = LocalDateTime.of(LocalDate.of(2023, 11, 23),
                LocalTime.MIDNIGHT);

        var missionCategoryResponse = MissionCategoryResponse.builder()
                .id(1L)
                .code("MC_001")
                .name("서빙")
                .build();

        var queryResponse = List.of((MainMissionQueryResponse) new MainMissionQueryTestDto());

        given(missionNativeQueryRepository.findSoonExpiredMissionByLocation(any(String.class), any(Integer.class), any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(queryResponse);

        var result = MissionSoonExpiredResponse.from(queryResponse.get(0), "s3::/path");

        // when
        var mainResponse = mainService.findMainResponse(userId, userPosition, serverTime);

        // then
        assertThat(mainResponse.missionCategories().get(0)).isEqualTo(missionCategoryResponse);
        assertThat(mainResponse.soonExpiredMissions().get(0)).isEqualTo(result);
    }

    private static class MainMissionQueryTestDto implements MainMissionQueryResponse {
        @Override
        public Long getId() {
            return 1L;
        }

        @Override
        public Long getCategoryId() {
            return 1L;
        }

        @Override
        public String getCategoryCode() {
            return "MC_001";
        }

        @Override
        public String getCategoryName() {
            return "서빙";
        }

        @Override
        public Long getCitizenId() {
            return 1L;
        }

        @Override
        public Long getRegionId() {
            return 1L;
        }

        @Override
        public String getSi() {
            return "서울시";
        }

        @Override
        public String getGu() {
            return "강남구";
        }

        @Override
        public String getDong() {
            return "역삼동";
        }

        @Override
        public Point getLocation() {
            return Mission.createPoint(121.66272838210327, 37.37898914571587);
        }

        @Override
        public String getTitle() {
            return "미션 제목";
        }

        @Override
        public LocalDate getMissionDate() {
            return LocalDate.of(2022, 11, 23);
        }

        @Override
        public LocalTime getStartTime() {
            return LocalTime.of(12, 30);
        }

        @Override
        public LocalTime getEndTime() {
            return LocalTime.of(13, 0);
        }

        @Override
        public LocalDateTime getDeadlineTime() {
            return LocalDateTime.of(LocalDate.of(2022, 11, 23),
                    LocalTime.of(12, 30));
        }

        @Override
        public Integer getPrice() {
            return 4000;
        }

        @Override
        public Integer getBookmarkCount() {
            return 3;
        }

        @Override
        public MissionStatus getMissionStatus() {
            return MissionStatus.MATCHING;
        }

        @Override
        public Long getBookmarkId() {
            return 1L;
        }
    }
}
