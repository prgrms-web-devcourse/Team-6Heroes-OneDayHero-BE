package com.sixheroes.onedayherodomain.mission;

import com.sixheroes.onedayherodomain.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "mission_bookmarks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "mission_and_user_id_idx",
                        columnNames = {"mission_id", "user_id"}
                )
        }
)
@Entity
public class MissionBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    private MissionBookmark(
            Mission mission,
            Long userId
    ) {
        this.mission = mission;
        this.userId = userId;
    }
}
