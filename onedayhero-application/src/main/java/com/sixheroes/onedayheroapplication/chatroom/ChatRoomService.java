package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayheroapplication.chatroom.repository.UserMissionChatRoomQueryRepository;
import com.sixheroes.onedayheroapplication.chatroom.repository.response.UserMissionChatRoomQueryResponse;
import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomCreateResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomExitResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomFindResponse;
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
    private final UserMissionChatRoomQueryRepository userMissionChatRoomQueryRepository;
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

    public List<MissionChatRoomFindResponse> findJoinedChatRoom(
            Long userId
    ) {
        var missionChatRooms = userMissionChatRoomQueryRepository.findJoinedChatRooms(userId);

        var chatRoomIds = getJoinedChatRoomIds(missionChatRooms);

        var receiverIds = getReceiverIds(userId, chatRoomIds);

        var receiverChatRoomInfos = userMissionChatRoomRepository.findReceiverChatRoomInfoInReceiverIds(receiverIds);
        // TODO 각 유저들에 대한 마지막으로 보낸 시간과 채팅을 찾는다. (toMap)
        // 5. 수신자들의 아이디로 마지막으로 보낸 시간과 채팅을 toMap으로 만든다.

        // 6. receiverChatRoomInfos 에 있는 receiverId와 동일한 부분을 합쳐서 response를 만든다.

        throw new UnsupportedOperationException();
    }

    private List<Long> getReceiverIds(Long userId, List<Long> chatRoomIds) {
        return userMissionChatRoomRepository.findByMissionChatRoom_IdIn(chatRoomIds)
                .stream()
                .filter(userMissionChatRoom -> !userMissionChatRoom.getUserId().equals(userId))
                .mapToLong(UserMissionChatRoom::getUserId)
                .boxed()
                .toList();
    }

    private List<Long> getJoinedChatRoomIds(List<UserMissionChatRoomQueryResponse> missionChatRooms) {
        return missionChatRooms.stream()
                .mapToLong(UserMissionChatRoomQueryResponse::chatRoomId)
                .boxed()
                .toList();
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

    private UserMissionChatRoom getUserChatRoom(Long userId, List<UserMissionChatRoom> findMissionChatRooms) {
        return findMissionChatRooms.stream()
                .filter(userMissionChatRoom -> userMissionChatRoom.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.T_001.name()));
    }
}
