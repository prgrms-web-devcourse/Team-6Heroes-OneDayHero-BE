package com.sixheroes.onedayherodomain.missionrequest;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionRequestTest {

    @DisplayName("요청한 유저가 제안 받은 히어로고 요청 중인 상태라면 미션 제안을 승낙한다.")
    @Test
    void changeMissionRequestStatusApprove() {
        // given
        var heroId = 1L;
        var missionRequest = createMissionRequest(heroId);

        // when
        missionRequest.changeMissionRequestStatusApprove(heroId);

        // then
        assertThat(missionRequest.getMissionRequestStatus()).isEqualTo(MissionRequestStatus.APPROVE);
    }

    @DisplayName("요청한 유저가 제안 받은 히어로가 아니면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Test
    void doNotchangeMissionRequestStatusApproveWhenNotHero() {
        // given
        var heroId = 1L;
        var requestUserId = 2L;
        var missionRequest = createMissionRequest(heroId);

        // when & then
        assertThatThrownBy(() -> missionRequest.changeMissionRequestStatusApprove(requestUserId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ErrorCode.EMR_001.name());
    }

    @DisplayName("제안 중인 상태가 아니라면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Test
    void doNotchangeMissionRequestStatusApproveWhenNotRequest() {
        // given
        var heroId = 1L;
        var missionRequest = createMissionRequest(heroId);
        missionRequest.changeMissionRequestStatusApprove(heroId);

        // when & then
        assertThatThrownBy(() -> missionRequest.changeMissionRequestStatusApprove(heroId))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage(ErrorCode.EMR_002.name());
    }

    public MissionRequest createMissionRequest(
        Long heroId
    ) {
        return MissionRequest.builder()
            .heroId(heroId)
            .missionId(1L)
            .build();
    }
}