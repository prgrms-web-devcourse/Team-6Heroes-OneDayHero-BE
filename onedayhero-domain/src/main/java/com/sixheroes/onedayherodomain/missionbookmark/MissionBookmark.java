package com.sixheroes.onedayherodomain.missionbookmark;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import com.sixheroes.onedayherodomain.mission.Mission;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_bookmarks")
@Entity
public class MissionBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Builder
    private MissionBookmark(Long userId, Mission mission) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(mission);
        this.userId = userId;
        this.mission = mission;
    }
}
