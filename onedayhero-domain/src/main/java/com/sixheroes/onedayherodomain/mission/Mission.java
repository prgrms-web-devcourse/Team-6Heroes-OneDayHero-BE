package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;

import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        this.bookmarkCount = 0;
        this.missionStatus = MissionStatus.MATCHING;
    }

    public void addBookmarkCount() {
        validateBookmarkCountAddable(this.missionStatus);
        this.bookmarkCount += 1;
    }

    public void subBookmarkCount() {
        this.bookmarkCount -= 1;
    }

    // TODO 미션이 매칭중이 아닐 때 validMissionRequestPossible, validMissionRequestApprove 검증
    public void validMissionRequestPossible(
        Long userId
    ) {
        validMissionOwner(userId);
        validMissionStatusMatching();
    }

    public void validMissionRequestApproveOrRejct() {
        validMissionStatusMatching();
    }

    private void validateBookmarkCountAddable(MissionStatus missionStatus) {
        if (missionStatus != MissionStatus.MATCHING) {
            log.warn("매칭중인 미션만 찜 할 수 있습니다. 미션 상태 : {}", missionStatus);
            throw new IllegalStateException(ErrorCode.EMC_002.name());
        }
    }

    public void validRangeOfMissionTime(LocalDateTime dateTime) {
        missionInfo.validMissionDateTimeInRange(dateTime);
    }

    private void validMissionOwner(
        Long userId
    ) {
        if (!citizenId.equals(userId)) {
            log.debug("미션 소유자가 아닙니다. userId : {}, citizenId : {}", userId, citizenId);
            throw new IllegalArgumentException(ErrorCode.EM_007.name());
        }
    }

    // TODO validateBookmarkCountAddable과 행위가 같음
    private void validMissionStatusMatching() {
        if (!missionStatus.isMatching()) {
            log.debug("미션 상태가 매칭 중이 아닙니다. missionStatus : {}", missionStatus);
            throw new IllegalStateException(ErrorCode.EM_008.name());
        }
    }
}
