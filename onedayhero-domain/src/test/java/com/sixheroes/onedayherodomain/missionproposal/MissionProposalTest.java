package com.sixheroes.onedayherodomain.missionproposal;

import com.sixheroes.onedayherocommon.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MissionProposalTest {

    @DisplayName("요청한 유저가 제안 받은 히어로고 요청 중인 상태라면 미션 제안을 승낙한다.")
    @Test
    void changeMissionProposalStatusApprove() {
        // given
        var heroId = 1L;
        var missionProposal = createMissionProposal(heroId);

        // when
        missionProposal.changeMissionProposalStatusApprove(heroId);

        // then
        assertThat(missionProposal.getMissionProposalStatus()).isEqualTo(MissionProposalStatus.APPROVE);
    }

    @DisplayName("요청한 유저가 제안 받은 히어로가 아니면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Test
    void doNotChangeMissionProposalStatusApproveWhenNotHero() {
        // given
        var heroId = 1L;
        var requestUserId = 2L;
        var missionProposal = createMissionProposal(heroId);

        // when & then
        assertThatThrownBy(() -> missionProposal.changeMissionProposalStatusApprove(requestUserId))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("제안 중인 상태가 아니라면 미션 제안을 승낙할 때 예외가 발생한다.")
    @Test
    void doNotchangeMissionProposalStatusApproveWhenNotProposal() {
        // given
        var heroId = 1L;
        var missionProposal = createMissionProposal(heroId);
        missionProposal.changeMissionProposalStatusApprove(heroId);

        // when & then
        assertThatThrownBy(() -> missionProposal.changeMissionProposalStatusApprove(heroId))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("요청한 유저가 제안 받은 히어로고 요청 중인 상태라면 미션 제안을 거절한다.")
    @Test
    void changeMissionRequestStatusReject() {
        // given
        var heroId = 1L;
        var missionProposal = createMissionProposal(heroId);

        // when
        missionProposal.changeMissionProposalStatusReject(heroId);

        // then
        assertThat(missionProposal.getMissionProposalStatus()).isEqualTo(MissionProposalStatus.REJECT);
    }

    @DisplayName("요청한 유저가 제안 받은 히어로가 아니면 미션 제안을 거절할 때 예외가 발생한다.")
    @Test
    void doNotchangeMissionProposalStatusRejectWhenNotHero() {
        // given
        var heroId = 1L;
        var requestUserId = 2L;
        var missionProposal = createMissionProposal(heroId);

        // when & then
        assertThatThrownBy(() -> missionProposal.changeMissionProposalStatusReject(requestUserId))
                .isInstanceOf(BusinessException.class);
    }

    @DisplayName("제안 중인 상태가 아니라면 미션 제안을 거절할 때 예외가 발생한다.")
    @Test
    void doNotchangeMissionProposalStatusRejctWhenNotProposal() {
        // given
        var heroId = 1L;
        var missionProposal = createMissionProposal(heroId);
        missionProposal.changeMissionProposalStatusApprove(heroId);

        // when & then
        assertThatThrownBy(() -> missionProposal.changeMissionProposalStatusReject(heroId))
                .isInstanceOf(BusinessException.class);
    }

    public MissionProposal createMissionProposal(
            Long heroId
    ) {
        return MissionProposal.builder()
                .heroId(heroId)
                .missionId(1L)
                .build();
    }
}