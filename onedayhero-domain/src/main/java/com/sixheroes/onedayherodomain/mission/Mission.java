package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import static java.util.Objects.requireNonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions")
@Entity
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
        requireNonNull(missionCategory);
        requireNonNull(citizenId);
        requireNonNull(regionId);
        requireNonNull(location);
        requireNonNull(missionInfo);
        requireNonNull(bookmarkCount);
        requireNonNull(missionStatus);
        this.missionCategory = missionCategory;
        this.citizenId = citizenId;
        this.regionId = regionId;
        this.location = location;
        this.missionInfo = missionInfo;
        this.bookmarkCount = bookmarkCount;
        this.missionStatus = missionStatus;
    }
}
