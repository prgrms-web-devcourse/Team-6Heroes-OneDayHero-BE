package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomResponse;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayheroinfrachat.repository.CustomMissionChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final MissionChatRoomReader missionChatRoomReader;
    private final CustomMissionChatRoomRepository missionChatRoomRepository;

    public MissionChatRoomResponse createChatRoom(
            String roomName
    ) {
        var missionChatRoom = MissionChatRoom.createMissionChatRoom(1L, roomName);
        var savedMissionChatRoom = missionChatRoomRepository.save(missionChatRoom);

        return MissionChatRoomResponse.from(savedMissionChatRoom);
    }

    public MissionChatRoomResponse findOne(
            Long missionChatRoomId
    ) {
        var missionChatRoom = missionChatRoomReader.findById(missionChatRoomId);

        return MissionChatRoomResponse.from(missionChatRoom);
    }

    public List<MissionChatRoomResponse> findAll() {
        var missionChatRoomResponses = missionChatRoomRepository.findAll()
                .stream()
                .map(MissionChatRoomResponse::from)
                .toList();

        return missionChatRoomResponses;
    }
}
