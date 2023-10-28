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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 20, nullable = false)
    private MissionCategoryCode missionCategoryCode;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Builder
    private MissionCategory(
            MissionCategoryCode missionCategoryCode,
            String name
    ) {
        this.missionCategoryCode = missionCategoryCode;
        this.name = name;
    }

    public static MissionCategory from(MissionCategoryCode missionCategoryCode) {
        return MissionCategory.builder()
                .missionCategoryCode(missionCategoryCode)
                .name(missionCategoryCode.getDescription())
                .build();
    }
}
