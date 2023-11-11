package com.sixheroes.onedayherodomain.missionchatroom;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE mission_chat_rooms SET is_disabled = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "mission_chat_rooms")
@Entity
public class MissionChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(name = "is_disabled", nullable = false)
    private Boolean isDisabled;

    @Builder
    private MissionChatRoom(
            Long missionId
    ) {
        this.missionId = missionId;
        this.isDisabled = false;
    }

    public static MissionChatRoom createMissionChatRoom(Long missionId) {
        return MissionChatRoom.builder()
                .missionId(missionId)
                .build();
    }

    public MissionChatRoom createMemoryMissionChatRoom(
            Long id,
            MissionChatRoom missionChatRoom
    ) {
        missionChatRoom.id = id;
        return missionChatRoom;
    }
}
