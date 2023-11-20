package com.sixheroes.onedayherodomain.missionchatroom;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_mission_chat_rooms")
@Entity
public class UserMissionChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_chat_room_id", nullable = false)
    private MissionChatRoom missionChatRoom;

    @Column(name = "is_joined", nullable = false)
    private Boolean isJoined;

    @Builder
    private UserMissionChatRoom(
            Long userId,
            MissionChatRoom missionChatRoom
    ) {
        this.userId = userId;
        this.missionChatRoom = missionChatRoom;
        this.isJoined = true;
    }

    public static UserMissionChatRoom createUserMissionChatRoom(
            Long userId,
            MissionChatRoom missionChatRoom
    ) {
        return UserMissionChatRoom.builder()
                .userId(userId)
                .missionChatRoom(missionChatRoom)
                .build();
    }

    public boolean isFindByUserId(Long userId) {
        return this.userId.equals(userId);
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void exit() {
        isJoined = false;
    }
}