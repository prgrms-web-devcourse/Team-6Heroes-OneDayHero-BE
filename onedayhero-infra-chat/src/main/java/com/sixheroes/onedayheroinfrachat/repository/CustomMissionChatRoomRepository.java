package com.sixheroes.onedayheroinfrachat.repository;

import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;

import java.util.List;
import java.util.Optional;

public interface CustomMissionChatRoomRepository {

    MissionChatRoom save(MissionChatRoom missionChatRoom);

    List<MissionChatRoom> findAll();

    Optional<MissionChatRoom> findById(Long missionChatRoomId);
}
