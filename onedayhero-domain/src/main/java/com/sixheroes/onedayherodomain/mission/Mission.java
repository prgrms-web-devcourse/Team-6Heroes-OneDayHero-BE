package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@SQLDelete(sql = "UPDATE missions SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "missions")
@Entity
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MissionCategory missionCategory;

    @Column(name = "citizen_id", nullable = false)
    private Long citizenId;

    @Column(name = "region_id", nullable = false)
    private Long regionId;

    @Column(name = "location", nullable = false)
    private Point location;

    @Embedded
    private MissionInfo missionInfo;

    @Column(name = "bookmark_count", nullable = false)
    private Integer bookmarkCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MissionStatus missionStatus;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder
    private Mission(
            MissionCategory missionCategory,
            Long citizenId,
            Long regionId,
            Point location,
            MissionInfo missionInfo,
            Integer bookmarkCount,
            MissionStatus missionStatus
    ) {
        this.missionCategory = missionCategory;
        this.citizenId = citizenId;
        this.regionId = regionId;
        this.location = location;
        this.missionInfo = missionInfo;
        this.bookmarkCount = bookmarkCount;
        this.missionStatus = missionStatus;
        this.isDeleted = false;
    }

    public static Mission createMission(
            MissionCategory missionCategory,
            Long citizenId,
            Long regionId,
            Point location,
            MissionInfo missionInfo
    ) {
        return Mission.builder().
                missionCategory(missionCategory)
                .citizenId(citizenId)
                .regionId(regionId)
                .location(location)
                .missionInfo(missionInfo)
                .bookmarkCount(0)
                .missionStatus(MissionStatus.MATCHING)
                .build();
    }

    public void update(Mission mission) {
        validOwn(mission.citizenId);
        validAbleUpdate();
        this.missionCategory = mission.missionCategory;
        this.missionInfo = mission.missionInfo;
        this.regionId = mission.regionId;
        this.location = mission.location;
    }

    public void validAbleDelete(Long citizenId) {
        validOwn(citizenId);

        if (missionStatus.isMatchingCompleted()) {
            throw new IllegalStateException(ErrorCode.EM_007.name());
        }
    }

    public void validRangeOfMissionTime(LocalDateTime dateTime) {
        missionInfo.validMissionDateTimeInRange(dateTime);
    }

    private void validOwn(Long citizenId) {
        if (!this.citizenId.equals(citizenId)) {
            log.warn("권한이 없는 사람이 시도하였습니다. id : {}", citizenId);
            throw new IllegalStateException(ErrorCode.EM_100.name());
        }
    }

    private void validAbleUpdate() {
        if (!missionStatus.isMatching()) {
            log.warn("미션을 수정할 수 없는 상태에서 시도하였습니다. missionStatus : {}", missionStatus.name());
            throw new IllegalStateException(ErrorCode.EM_009.name());
        }
    }
}
