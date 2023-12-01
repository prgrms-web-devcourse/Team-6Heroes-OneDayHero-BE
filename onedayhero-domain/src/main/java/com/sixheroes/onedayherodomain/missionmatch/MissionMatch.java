package com.sixheroes.onedayherodomain.missionmatch;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_matches")
@Entity
public class MissionMatch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "hero_id", nullable = false)
    private Long heroId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MissionMatchStatus missionMatchStatus;

    @Builder
    private MissionMatch(
            Long missionId,
            Long heroId,
            MissionMatchStatus missionMatchStatus
    ) {
        this.missionId = missionId;
        this.heroId = heroId;
        this.missionMatchStatus = missionMatchStatus;
    }

    public static MissionMatch createMissionMatch(
            Long missionId,
            Long heroId
    ) {
        return MissionMatch.builder()
                .missionId(missionId)
                .heroId(heroId)
                .missionMatchStatus(MissionMatchStatus.MATCHED)
                .build();
    }

    public boolean isMatchedHero(
        Long userId
    ) {
        return Objects.equals(this.heroId, userId);
    }

    public void canceled() {
        validateCurrentMissionMatchStatusIsMatchingMatched();
        this.missionMatchStatus = MissionMatchStatus.CANCELED;
    }

    private void validateCurrentMissionMatchStatusIsMatchingMatched() {
        if (this.missionMatchStatus != MissionMatchStatus.MATCHED) {
            log.debug("매칭된 상태의 미션만 취소할 수 있습니다. 미션 상태 : {}", this.missionMatchStatus);
            throw new BusinessException(ErrorCode.INVALID_MATCHING_STATUS);
        }
    }
}
