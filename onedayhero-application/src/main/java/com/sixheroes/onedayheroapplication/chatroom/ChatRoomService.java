package com.sixheroes.onedayheroapplication.chatroom;

import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomCreateResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomExitResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomFindResponse;
import com.sixheroes.onedayherochat.application.repository.MissionChatRoomRedisRepository;
import com.sixheroes.onedayherochat.application.repository.request.MissionChatRoomRedisRequest;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import com.sixheroes.onedayherocommon.exception.EntityNotFoundException;
import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.UserMissionChatRoom;
import com.sixheroes.onedayherodomain.missionchatroom.repository.MissionChatRoomRepository;
import com.sixheroes.onedayherodomain.missionchatroom.repository.UserMissionChatRoomRepository;
import com.sixheroes.onedayherodomain.missionchatroom.repository.response.UserChatRoomQueryResponse;
import com.sixheroes.onedayheromongo.chat.ChatMessage;
import com.sixheroes.onedayheromongo.chat.repository.ChatMessageMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomService {

    private static final int MAX_CAPACITY = 2;
    private final MissionChatRoomValidator missionChatRoomValidator;
    private final MissionChatRoomReader missionChatRoomReader;
    private final MissionChatRoomRepository missionChatRoomRepository;
    private final UserMissionChatRoomRepository userMissionChatRoomRepository;
    private final UserMissionChatRoomReader userMissionChatRoomReader;
    private final MissionChatRoomRedisRepository missionChatRoomRedisRepository;
    private final ChatMessageMongoRepository chatMessageMongoRepository;

    @Transactional
    public MissionChatRoomCreateResponse createChatRoom(
            CreateMissionChatRoomServiceRequest request
    ) {
        missionChatRoomValidator.duplicateMissionChatRoom(request.userIds(), request.missionId());
        var missionChatRoom = MissionChatRoom.createMissionChatRoom(request.missionId(), request.userIds());

        var savedMissionChatRoom = missionChatRoomRepository.save(missionChatRoom);
        missionChatRoomRedisRepository.create(MissionChatRoomRedisRequest.from(missionChatRoom));

        return MissionChatRoomCreateResponse.from(savedMissionChatRoom);
    }

    public List<MissionChatRoomFindResponse> findJoinedChatRoom(
            Long userId
    ) {
        // 유저가 입장 상태인 채팅방을 가져온다.
        var missionChatRooms = userMissionChatRoomRepository.findByUserIdAndIsJoinedTrue(userId);

        var chatRoomIds = getJoinedChatRoomIds(missionChatRooms);

        // 유저가 입장 상태인 채팅방의 수신자 정보를 가지고 온다.
        var receiverChatRoomInfos = userMissionChatRoomRepository.findReceiverChatRoomInfoInChatRoomIdsAndUserId(chatRoomIds, userId);

        // 유저가 입장 상태인 채팅방의 마지막 메시지를 가지고 온다.
        var latestMessagesByChatRoomIds = chatMessageMongoRepository.findLatestMessagesByChatRoomIds(chatRoomIds);

        var missionChatRoomFindResponses = makeMissionChatRoomResponse(receiverChatRoomInfos, latestMessagesByChatRoomIds);

        return missionChatRoomFindResponses;
    }

    @Transactional
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

    // 유저가 나가지 않은 채팅방에 속한 다른 유저들을 조회한다.
    private List<Long> getJoinedChatRoomIds(
            List<UserMissionChatRoom> userMissionChatRooms
    ) {
        return userMissionChatRooms.stream()
                .mapToLong((userMissionChatRoom) -> userMissionChatRoom.getMissionChatRoom().getId())
                .boxed()
                .toList();
    }

    // 똑같은 채팅방 ID에 속한 값으로 채팅방 목록 응답 값을 생성한다.
    private List<MissionChatRoomFindResponse> makeMissionChatRoomResponse(
            List<UserChatRoomQueryResponse> receiverChatRoomInfos,
            List<ChatMessage> latestMessagesByChatRoomIds
    ) {
        var result = new ArrayList<MissionChatRoomFindResponse>();

        var chatRoomInfoGroupingByChatRoomId = receiverChatRoomInfos.stream()
                .collect(Collectors.groupingBy(UserChatRoomQueryResponse::chatRoomId));

        var chatMessageGroupingByChatRoomId = latestMessagesByChatRoomIds.stream()
                .collect(Collectors.groupingBy(ChatMessage::getChatRoomId));

        for (Long chatRoomId : chatRoomInfoGroupingByChatRoomId.keySet()) {
            var chatRoomQueryResponse = chatRoomInfoGroupingByChatRoomId.get(chatRoomId).get(0);
            var lastMessage = chatMessageGroupingByChatRoomId.containsKey(chatRoomId) ? chatMessageGroupingByChatRoomId.get(chatRoomId).get(0) : null;

            var missionChatRoomInfo = MissionChatRoomFindResponse.from(chatRoomQueryResponse, lastMessage);
            result.add(missionChatRoomInfo);
        }

        return result;
    }

    private UserMissionChatRoom getUserChatRoom(
            Long userId,
            List<UserMissionChatRoom> findMissionChatRooms
    ) {
        return findMissionChatRooms.stream()
                .filter(userMissionChatRoom -> userMissionChatRoom.isUserChatRoom(userId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("타인의 유저 채팅방이 존재하지 않습니다. userId : {}", userId);
                    return new EntityNotFoundException(ErrorCode.NOT_FOUND_MISSION_CHATROOM);
                });
    }

    public MissionChatRoomCreateResponse findOne(
            Long missionChatRoomId
    ) {
        var missionChatRoom = missionChatRoomReader.findById(missionChatRoomId);

        return MissionChatRoomCreateResponse.from(missionChatRoom);
    }
}
