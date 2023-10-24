package com.sixheroes.onedayherodomain.mission;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_category")
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
}
