package com.sixheroes.onedayherodomain.mission;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(name = "mission_id", nullable = false)
    private Long mission_id;

    @Column(name = "original_name", nullable = false)
    private String original_name;

    @Column(name = "unique_name", nullable = false)
    private String unique_name;

    @Column(name = "paths", nullable = false)
    private String path;
}
