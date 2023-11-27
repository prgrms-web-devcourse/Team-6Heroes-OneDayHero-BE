package com.sixheroes.onedayherodomain.missionproposal;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_proposals",
        uniqueConstraints = {
                // TODO 제약조건 위반 막기
                @UniqueConstraint(columnNames = {"mission_id", "hero_id"})
        }
)
@Entity
public class MissionProposal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "hero_id", nullable = false)
    private Long heroId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MissionProposalStatus missionProposalStatus;

    @Builder
    private MissionProposal(
            Long missionId,
            Long heroId
    ) {
        this.missionId = missionId;
        this.heroId = heroId;
        this.missionProposalStatus = MissionProposalStatus.PROPOSAL;
    }

    public void changeMissionProposalStatusApprove(
            Long userId
    ) {
        validMissionProposalStatus();
        validHeroId(userId);

        this.missionProposalStatus = MissionProposalStatus.APPROVE;
    }

    public void changeMissionProposalStatusReject(
            Long userId
    ) {
        validMissionProposalStatus();
        validHeroId(userId);

        this.missionProposalStatus = MissionProposalStatus.REJECT;
    }

    private void validMissionProposalStatus() {
        if (!missionProposalStatus.isProposal()) {
            log.debug("미션 제안 중인 상태가 아닙니다. missionProposaltStatus : {}", missionProposalStatus);
            throw new BusinessException(ErrorCode.INVALID_MISSION_PROPOSAL_STATUS);
        }
    }

    private void validHeroId(
            Long userId
    ) {
        if (!this.heroId.equals(userId)) {
            log.debug("요청한 유저는 미션을 제안 받은 히어로가 아닙니다. userId : {}, heroId : {}", userId, heroId);
            throw new BusinessException(ErrorCode.INVALID_MISSION_PROPOSAL_HERO);
        }
    }
}