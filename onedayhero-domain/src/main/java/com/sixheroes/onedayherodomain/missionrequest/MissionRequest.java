package com.sixheroes.onedayherodomain.missionrequest;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_requests")
@Entity
public class MissionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "hero_id", nullable = false)
    private Long heroId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MissionRequestStatus missionRequestStatus;
}
