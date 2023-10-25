package com.sixheroes.onedayherodomain.mission;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions")
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citizen_id", nullable = false)
    private Long citizenId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "region_id", nullable = false)
    private Long regionId;

    @Column(name = "location", nullable = false)
    private Point location;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "mission_date", nullable = false)
    private LocalDate missionDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "bookmark_count", nullable = false)
    private Integer bookmarkCount;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "deadline_time", nullable = false)
    private LocalTime deadlineTime;

    // MissionStatus
    // MATCHING, MATCHING_COMPLETE, COMPLETE, EXPIRE
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MissionStatus missionStatus;
}
