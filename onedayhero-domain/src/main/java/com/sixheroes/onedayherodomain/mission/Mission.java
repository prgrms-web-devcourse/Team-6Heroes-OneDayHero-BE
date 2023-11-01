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

    public void missionMatchingCompleted(Long userId) {
        validateMissionOwnerIsValid(userId);
        validateCurrentMissionStatusIsMatching();
        this.missionStatus = MissionStatus.MATCHING_COMPLETED;
    }

    public void missionMatchingCanceled(Long citizenId) {
        validateMissionOwnerIsValid(citizenId);
        validateCurrentMissionStatusIsMatchingCompleted();
        this.missionStatus = MissionStatus.MATCHING;
    }

    public void addBookmarkCount() {
        validateBookmarkCountAddable();
        this.bookmarkCount += 1;
    }

    public void subBookmarkCount() {
        this.bookmarkCount -= 1;
    }

    private void validateMissionOwnerIsValid(Long citizenId) {
        if (!this.citizenId.equals(citizenId)) {
            throw new IllegalStateException(ErrorCode.EM_008.name());
        }
    }

    private void validateCurrentMissionStatusIsMatching() {
        if (this.missionStatus != MissionStatus.MATCHING) {
            log.debug("매칭 중 상태인 미션에 대해서만 매칭완료 설정을 할 수 있습니다. 미션 상태 : {}", this.missionStatus);
            throw new IllegalStateException(ErrorCode.EM_007.name());
        }
    }

    private void validateCurrentMissionStatusIsMatchingCompleted() {
        if (this.missionStatus != MissionStatus.MATCHING_COMPLETED) {
            log.debug("매칭 완료인 상태의 미션만 포기/철회 상태로 설정할 수 있습니다. 미션 상태 : {}", this.missionStatus);
            throw new IllegalStateException(ErrorCode.EM_009.name());
        }
    }

    private void validateBookmarkCountAddable() {
        if (this.missionStatus != MissionStatus.MATCHING) {
            log.debug("매칭중인 미션만 찜 할 수 있습니다. 미션 상태 : {}", this.missionStatus);
            throw new IllegalStateException(ErrorCode.EMC_002.name());
        }
    }

    public void validRangeOfMissionTime(LocalDateTime dateTime) {
        missionInfo.validMissionDateTimeInRange(dateTime);
    }
}
