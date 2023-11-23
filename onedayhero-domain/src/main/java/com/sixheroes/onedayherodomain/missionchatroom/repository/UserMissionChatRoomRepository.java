package com.sixheroes.onedayherodomain.missionchatroom.repository;

import com.sixheroes.onedayherodomain.missionchatroom.UserMissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.repository.response.UserChatRoomQueryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserMissionChatRoomRepository extends JpaRepository<UserMissionChatRoom, Long> {

    @Query("select um from UserMissionChatRoom um join fetch um.missionChatRoom where um.missionChatRoom.id = :chatRoomId and um.isJoined = true")
    List<UserMissionChatRoom> findByMissionChatRoom_Id(Long chatRoomId);

    List<UserMissionChatRoom> findByMissionChatRoom_IdIn(List<Long> missionChatRoomIds);

    @Query("""
            SELECT NEW com.sixheroes.onedayherodomain.missionchatroom.repository.response.UserChatRoomQueryResponse(
            mr.id, u.id, m.id, m.missionInfo.title, u.userBasicInfo.nickname, ui.path, mr.headCount
            )
            from UserMissionChatRoom um
            join MissionChatRoom mr
            on um.missionChatRoom.id = mr.id
            join Mission m
            on mr.missionId = m.id
            join User u
            on u.id = um.userId
            join UserImage ui
            on u.id = ui.id
            where um.userId in :receiverIds
            """)
    List<UserChatRoomQueryResponse> findReceiverChatRoomInfoInReceiverIds(List<Long> receiverIds);
}