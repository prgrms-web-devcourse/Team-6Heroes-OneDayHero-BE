package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.BusinessException;
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

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL)
    List<MissionImage> missionImages = new ArrayList<>();

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

    public void addMissionImages(
            List<MissionImage> missionImages
    ) {
        missionImages.forEach(missionImage -> {
            missionImage.setMission(this);
            this.missionImages.add(missionImage);
        });
    }

    public void update(
            Mission mission,
            Long userId
    ) {
        validOwn(userId);
        validAbleUpdate();
        this.missionCategory = mission.missionCategory;
        this.missionInfo = mission.missionInfo;
        this.regionId = mission.regionId;
        this.location = mission.location;
    }

    public void extend(
            MissionInfo missionInfo,
            Long userId
    ) {
        validOwn(userId);
        validAbleExtend();
        this.missionInfo = missionInfo;
        this.missionStatus = MissionStatus.MATCHING;
    }

    public void complete(
            Long userId
    ) {
        validOwn(userId);
        validAbleComplete();
        this.missionStatus = MissionStatus.MISSION_COMPLETED;
    }

    public void validAbleDelete(
            Long citizenId
    ) {
        validOwn(citizenId);

        if (missionStatus.isMatchingCompleted()) {
            throw new BusinessException(ErrorCode.ABORT_MISSION_DELETE);
        }
    }

    private void validAbleExtend() {
        if (!missionStatus.isExpired()) {
            log.warn("미션의 연장이 불가능한 상태입니다. 현재 상태 : {}", missionStatus.getDescription());
            throw new BusinessException(ErrorCode.ABORT_MISSION_EXTEND);
        }
    }

    private void validAbleComplete() {
        if (!missionStatus.isMatchingCompleted()) {
            log.warn("미션을 완료 상태로 변경이 불가능한 상태입니다. 현재 상태 : {}", missionStatus.getDescription());
            throw new BusinessException(ErrorCode.ABORT_MISSION_COMPLETE);
        }
    }

    public void completeMissionMatching(Long userId) {
        validOwn(userId);
        validateCurrentMissionStatusIsMatching();
        this.missionStatus = MissionStatus.MATCHING_COMPLETED;
    }

    public void cancelMissionMatching(Long citizenId) {
        validOwn(citizenId);
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

    public void validateMissionCompleted() {
        if (this.missionStatus != MissionStatus.MISSION_COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_MISSION_UPDATE_REQUEST);
        }
    }

    // TODO 미션이 매칭중이 아닐 때 validMissionProposalPossible, validMissionProposalChangeStatus 검증
    public void validMissionProposalPossible(
            Long userId
    ) {
        validOwn(userId);
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
            Long userId
    ) {
        if (!this.citizenId.equals(userId)) {
            log.warn("미션 소유자가 아닙니다. userId : {}, citizenId : {}", userId, citizenId);
            throw new BusinessException(ErrorCode.INVALID_MISSION_OWNER);
        }
    }

    private void validAbleUpdate() {
        if (!missionStatus.isMatching()) {
            log.warn("미션을 수정할 수 없는 상태에서 시도하였습니다. missionStatus : {}", missionStatus.name());
            throw new BusinessException(ErrorCode.ABORT_MISSION_UPDATE);
        }
    }

    // TODO validateBookmarkCountAddable과 행위가 같음
    private void validMissionStatusMatching() {
        if (!this.missionStatus.isMatching()) {
            log.warn("미션 상태가 매칭 중이 아닙니다. missionStatus : {}", missionStatus);
            throw new BusinessException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    private void validateCurrentMissionStatusIsMatching() {
        if (this.missionStatus != MissionStatus.MATCHING) {
            log.warn("매칭 중 상태인 미션에 대해서만 매칭완료 설정을 할 수 있습니다. 미션 상태 : {}", this.missionStatus);
            throw new BusinessException(ErrorCode.INVALID_MATCHING_STATUS);
        }
    }

    private void validateCurrentMissionStatusIsMatchingCompleted() {
        if (this.missionStatus != MissionStatus.MATCHING_COMPLETED) {
            log.warn("매칭 완료인 상태의 미션만 포기/철회 상태로 설정할 수 있습니다. 미션 상태 : {}", this.missionStatus);
            throw new BusinessException(ErrorCode.INVALID_MATCHING_STATUS);
        }
    }

    private void validateBookmarkCountAddable() {
        if (this.missionStatus != MissionStatus.MATCHING) {
            log.warn("매칭중인 미션만 찜 할 수 있습니다. 미션 상태 : {}", this.missionStatus);
            throw new BusinessException(ErrorCode.INVALID_STATUS_MISSION_BOOKMARK_REQUEST);
        }
    }
}
