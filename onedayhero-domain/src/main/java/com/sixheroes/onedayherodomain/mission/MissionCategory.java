package com.sixheroes.onedayherodomain.mission;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "m_categories")
@Entity
public class MissionCategory {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 20, nullable = false)
    private MissionCategoryCode missionCategoryCode;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Builder
    private MissionCategory(
            Long id,
            MissionCategoryCode missionCategoryCode,
            String name
    ) {
        this.id = id;
        this.missionCategoryCode = missionCategoryCode;
        this.name = name;
    }

    public static MissionCategory createMissionCategory(
            MissionCategoryCode missionCategoryCode
    ) {
        return MissionCategory.builder()
                .id(missionCategoryCode.getCategoryId())
                .missionCategoryCode(missionCategoryCode)
                .name(missionCategoryCode.getDescription())
                .build();
    }
}
