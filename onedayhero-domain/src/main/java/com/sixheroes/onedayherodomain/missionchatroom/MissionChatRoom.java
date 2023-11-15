package com.sixheroes.onedayherodomain.missionchatroom;


import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @Column(name = "head_count", nullable = false)
    private Integer headCount;

    @OneToMany(mappedBy = "missionChatRoom", cascade = CascadeType.ALL)
    List<UserMissionChatRoom> userMissionChatRooms = new ArrayList<>();

    @Column(name = "is_disabled", nullable = false)
    private Boolean isDisabled;

    @Builder
    private MissionChatRoom(
            Long missionId,
            List<Long> userIds
    ) {
        this.missionId = missionId;
        this.userMissionChatRooms = createUserMissionChatRooms(userIds);
        this.headCount = 2;
        this.isDisabled = false;
    }

    public static MissionChatRoom createMissionChatRoom(
            Long missionId,
            List<Long> userIds
    ) {
        return MissionChatRoom.builder()
                .missionId(missionId)
                .userIds(userIds)
                .build();
    }

    private List<UserMissionChatRoom> createUserMissionChatRooms(List<Long> userIds) {
        validUserCount(userIds);
        return userIds.stream()
                .map((userId) -> UserMissionChatRoom.createUserMissionChatRoom(userId, this))
                .toList();
    }

    private void validUserCount(List<Long> userIds) {
        if (userIds.size() != 2) {
            log.debug("채팅방을 생성하는데 필요한 인원이 들어오지 않았습니다. userCount : {}", userIds.size());
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }
    }
}
