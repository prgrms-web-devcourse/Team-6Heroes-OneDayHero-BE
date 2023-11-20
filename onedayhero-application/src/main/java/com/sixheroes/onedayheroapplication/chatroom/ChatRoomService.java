package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomCreateResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomExitResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomFindResponses;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisRepository;
import com.sixheroes.onedayherochat.application.repository.request.MissionChatRoomRedisRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.UserMissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.repository.MissionChatRoomRepository;
import com.sixheroes.onedayherodomain.missionchatroom.repository.UserMissionChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private static final int MAX_CAPACITY = 2;
    private final MissionChatRoomReader missionChatRoomReader;
    private final MissionChatRoomRepository missionChatRoomRepository;
    private final UserMissionChatRoomRepository userMissionChatRoomRepository;
    private final UserMissionChatRoomReader userMissionChatRoomReader;
    private final MissionChatRoomRedisRepository missionChatRoomRedisRepository;

    public MissionChatRoomCreateResponse createChatRoom(
            CreateMissionChatRoomServiceRequest request
    ) {
        var missionChatRoom = MissionChatRoom.createMissionChatRoom(request.missionId(), request.userIds());
        var savedMissionChatRoom = missionChatRoomRepository.save(missionChatRoom);
        missionChatRoomRedisRepository.create(MissionChatRoomRedisRequest.from(missionChatRoom));

        return MissionChatRoomCreateResponse.from(savedMissionChatRoom);
    }

    public MissionChatRoomFindResponses findJoinedChatRoom(
            Long userId
    ) {
        var missionChatRoomResponses = userMissionChatRoomRepository.findByUserId(userId)
                .stream()
                .map((userMissionChatRoom) -> MissionChatRoomCreateResponse.from(userMissionChatRoom.getMissionChatRoom()))
                .toList();

        // userId가 현재 들어가 있는 모든 채팅방을 찾는다.

        throw new UnsupportedOperationException();
    }

    public MissionChatRoomExitResponse exitChatRoom(
            Long chatRoomId,
            Long userId
    ) {
        var findMissionChatRooms = userMissionChatRoomReader.findByChatRoomId(chatRoomId);

        if (findMissionChatRooms.size() == MAX_CAPACITY) {
            var userChatRoom = getUserChatRoom(userId, findMissionChatRooms);
            userChatRoom.exit();
            return MissionChatRoomExitResponse.from(userChatRoom.getMissionChatRoom(), userId);
        }

        var userChatRoom = getUserChatRoom(userId, findMissionChatRooms);
        userChatRoom.exit();

        missionChatRoomRedisRepository.removeChatRoom(chatRoomId);
        missionChatRoomRepository.delete(userChatRoom.getMissionChatRoom());
        return MissionChatRoomExitResponse.from(userChatRoom.getMissionChatRoom(), userId);
    }

    public MissionChatRoomCreateResponse findOne(
            Long missionChatRoomId
    ) {
        var missionChatRoom = missionChatRoomReader.findById(missionChatRoomId);

        return MissionChatRoomCreateResponse.from(missionChatRoom);
    }

    private UserMissionChatRoom getUserChatRoom(
            Long userId,
            List<UserMissionChatRoom> findMissionChatRooms
    ) {
        return findMissionChatRooms.stream()
                .filter(userMissionChatRoom -> userMissionChatRoom.isFindByUserId(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.T_001.name()));
    }
}
