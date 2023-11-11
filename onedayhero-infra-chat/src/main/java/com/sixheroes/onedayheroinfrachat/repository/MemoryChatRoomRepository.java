package com.sixheroes.onedayheroinfrachat.repository;

import com.sixheroes.onedayherodomain.missionchatroom.MissionChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Repository
public class MemoryChatRoomRepository implements CustomMissionChatRoomRepository {

    private static Long missionChatRoomId = 0L;
    private ConcurrentSkipListMap<Long, MissionChatRoom> missionChatRoomStorage;

    @PostConstruct
    private void initStorage() {
        // ConcurrentSkipListMap 은 TreeMap의 Concurrent와 유사합니다.
        missionChatRoomStorage = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    }

    @Override
    public MissionChatRoom save(
            MissionChatRoom missionChatRoom
    ) {
        missionChatRoom.createMemoryMissionChatRoom(++missionChatRoomId, missionChatRoom);
        missionChatRoomStorage.put(missionChatRoomId, missionChatRoom);
        return missionChatRoom;
    }

    @Override
    public List<MissionChatRoom> findAll() {
        return missionChatRoomStorage.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<MissionChatRoom> findById(
            Long missionChatRoomId
    ) {
        if (!missionChatRoomStorage.containsKey(missionChatRoomId)) {
            return Optional.empty();
        }

        return Optional.of(missionChatRoomStorage.get(missionChatRoomId));
    }
}
