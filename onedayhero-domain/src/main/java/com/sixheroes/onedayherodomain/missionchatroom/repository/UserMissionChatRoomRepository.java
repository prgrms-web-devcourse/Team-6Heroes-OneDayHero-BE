package com.sixheroes.onedayherodomain.missionchatroom.repository;

import com.sixheroes.onedayherodomain.missionchatroom.UserMissionChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserMissionChatRoomRepository extends JpaRepository<UserMissionChatRoom, Long> {

    @Query("select um from UserMissionChatRoom um join fetch um.missionChatRoom where um.userId = :userId and um.isJoined = true")
    List<UserMissionChatRoom> findByUserId(Long userId);

    @Query("select um from UserMissionChatRoom um join fetch um.missionChatRoom where um.missionChatRoom.id = :chatRoomId and um.isJoined = true")
    List<UserMissionChatRoom> findByMissionChatRoom_Id(Long chatRoomId);
}