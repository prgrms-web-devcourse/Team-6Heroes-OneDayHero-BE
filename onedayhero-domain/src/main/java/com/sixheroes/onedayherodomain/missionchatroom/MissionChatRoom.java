package com.sixheroes.onedayherodomain.missionchatroom;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_chat_rooms")
@Entity
public class MissionChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "hero_id", nullable = false)
    private Long heroId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
