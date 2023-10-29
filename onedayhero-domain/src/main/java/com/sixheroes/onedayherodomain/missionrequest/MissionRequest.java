package com.sixheroes.onedayherodomain.missionrequest;

<<<<<<< HEAD
import com.sixheroes.onedayherocommon.error.ErrorCode;
=======
>>>>>>> 4b6dffc ([SIX-98] feat & test : 미션 제안 생성에 대한 도메인 로직 구현 및 테스트)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_requests")
@Entity
public class MissionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "hero_id", nullable = false)
    private Long heroId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MissionRequestStatus missionRequestStatus;

    @Builder
    private MissionRequest(
        Long missionId,
        Long heroId
    ) {
        this.missionId = missionId;
        this.heroId = heroId;
        this.missionRequestStatus = MissionRequestStatus.REQUEST;
    }
<<<<<<< HEAD

    public void changeMissionRequestStatusApprove(
        Long userId
    ) {
        validMissionRequestStatus();
        validHeroId(userId);

        this.missionRequestStatus = MissionRequestStatus.APPROVE;
    }

    public void changeMissionRequestStatusReject(
        Long userId
    ) {
        validMissionRequestStatus();
        validHeroId(userId);

        this.missionRequestStatus = MissionRequestStatus.REJECT;
    }

    private void validMissionRequestStatus() {
        if (!missionRequestStatus.isRequest()) {
            log.debug("미션 제안 중인 상태가 아닙니다. missionRequestStatus : {}", missionRequestStatus);
            throw new IllegalStateException(ErrorCode.EMR_002.name());
        }
    }

    private void validHeroId(
        Long userId
    ) {
        if (!this.heroId.equals(userId)) {
            log.debug("요청한 유저는 미션을 제안 받은 히어로가 아닙니다. userId : {}, heroId : {}", userId, heroId);
            throw new IllegalArgumentException(ErrorCode.EMR_001.name());
        }
    }
}