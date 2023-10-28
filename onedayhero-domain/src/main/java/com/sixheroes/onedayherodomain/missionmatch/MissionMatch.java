package com.sixheroes.onedayherodomain.missionmatch;

import com.sixheroes.onedayherodomain.mission.MissionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_matches")
@Entity
public class MissionMatch {

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
}
