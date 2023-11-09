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
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Objects;

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

    // TODO : 미션 생성 시 전달 받는 값을 longitude, latitude 로 분리하고 생성자에서 Point 변환
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

    public static Point createPoint(double x, double y) {
        var geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    public static Mission createMission(
            MissionCategory missionCategory,
            Long citizenId,
            Long regionId,
            double longitude,
            double latitude,
            MissionInfo missionInfo
    ) {
        return Mission.builder().
                missionCategory(missionCategory)
                .citizenId(citizenId)
                .regionId(regionId)
                .location(createPoint(longitude, latitude))
                .missionInfo(missionInfo)
                .bookmarkCount(0)
                .missionStatus(MissionStatus.MATCHING)
                .build();
    }

    public void update(
            Mission mission
    ) {
        validOwn(mission.citizenId);
        validAbleUpdate();
        this.missionCategory = mission.missionCategory;
        this.missionInfo = mission.missionInfo;
        this.regionId = mission.regionId;
        this.location = mission.location;
    }

    public void extend(
            Mission mission
    ) {
        validOwn(mission.citizenId);
        validAbleExtend();
        this.missionCategory = mission.missionCategory;
        this.missionInfo = mission.missionInfo;
        this.regionId = mission.regionId;
        this.location = mission.location;
        this.missionStatus = MissionStatus.MATCHING;
    }

    public void validAbleDelete(
            Long citizenId
    ) {
        validOwn(citizenId);

        if (missionStatus.isMatchingCompleted()) {
            throw new IllegalStateException(ErrorCode.EM_007.name());
        }
    }

    private void validAbleExtend() {
        if (!missionStatus.isExpired()) {
            log.debug("미션의 연장이 불가능한 상태입니다. 현재 상태 : {}", missionStatus.getDescription());
            throw new IllegalStateException(ErrorCode.T_001.name());
        }
    }

    public void completeMissionMatching(Long userId) {
        validateMissionOwnerIsValid(userId);
        validateCurrentMissionStatusIsMatching();
        this.missionStatus = MissionStatus.MATCHING_COMPLETED;
    }

    public void cancelMissionMatching(Long citizenId) {
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
        }
    }

    // TODO 미션이 매칭중이 아닐 때 validMissionProposalPossible, validMissionProposalChangeStatus 검증
    public void validMissionProposalPossible(
        Long userId
    ) {
        validMissionOwner(userId);
        validMissionStatusMatching();
    }

    public void validMissionProposalChangeStatus() {
        validMissionStatusMatching();
    }

    // TODO 미션 상태 변화하는 메서드 지워야함.
    public void changeMissionStatus(
            MissionStatus missionStatus
    ) {
        this.missionStatus = missionStatus;
    }

    private void validOwn(
            Long citizenId
    ) {
        if (!this.citizenId.equals(citizenId)) {
            log.debug("권한이 없는 사람이 시도하였습니다. id : {}", citizenId);
            throw new IllegalStateException(ErrorCode.EM_100.name());
        }
    }

    private void validAbleUpdate() {
        if (!missionStatus.isMatching()) {
            log.debug("미션을 수정할 수 없는 상태에서 시도하였습니다. missionStatus : {}", missionStatus.name());
            throw new IllegalStateException(ErrorCode.EM_009.name());
        }
    }

    private void validMissionOwner(
        Long userId
    ) {
        if (!Objects.equals(this.citizenId, userId)) {
            log.debug("미션 소유자가 아닙니다. userId : {}, citizenId : {}", userId, citizenId);
            throw new IllegalArgumentException(ErrorCode.EM_007.name());
        }
    }

    // TODO validateBookmarkCountAddable과 행위가 같음
    private void validMissionStatusMatching() {
        if (!this.missionStatus.isMatching()) {
            log.debug("미션 상태가 매칭 중이 아닙니다. missionStatus : {}", missionStatus);
            throw new IllegalStateException(ErrorCode.EM_008.name());
        }
    }
}
