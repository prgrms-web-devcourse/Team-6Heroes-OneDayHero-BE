package com.sixheroes.onedayherodomain.mission;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_images")
@Entity
public class MissionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(name = "original_name", length = 260, nullable = false)
    private String originalName;

    @Column(name = "unique_name", length = 100, nullable = false)
    private String uniqueName;

    @Column(name = "path", length = 250, nullable = false)
    private String path;

    @Builder
    private MissionImage(
            String originalName,
            String uniqueName,
            String path
    ) {
        this.originalName = originalName;
        this.uniqueName = uniqueName;
        this.path = path;
    }

    public void setMission(
            Mission mission
    ) {
        this.mission = mission;
    }

    public static MissionImage createMissionImage(
            String originalName,
            String uniqueName,
            String path
    ) {
        return MissionImage.builder()
                .originalName(originalName)
                .uniqueName(uniqueName)
                .path(path)
                .build();
    }
}
