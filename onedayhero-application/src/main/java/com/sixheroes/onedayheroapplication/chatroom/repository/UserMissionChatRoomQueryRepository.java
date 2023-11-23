package com.sixheroes.onedayheroapplication.chatroom.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sixheroes.onedayheroapplication.chatroom.repository.response.UserMissionChatRoomQueryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sixheroes.onedayherodomain.mission.QMission.mission;
import static com.sixheroes.onedayherodomain.missionchatroom.QMissionChatRoom.missionChatRoom;
import static com.sixheroes.onedayherodomain.missionchatroom.QUserMissionChatRoom.userMissionChatRoom;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserMissionChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<UserMissionChatRoomQueryResponse> findJoinedChatRooms(
            Long userId
    ) {
        return queryFactory.select(
                        Projections.constructor(UserMissionChatRoomQueryResponse.class,
                                userMissionChatRoom.id,
                                userMissionChatRoom.missionChatRoom.id,
                                userMissionChatRoom.missionChatRoom.missionId,
                                userMissionChatRoom.missionChatRoom.headCount,
                                mission.missionInfo.title))
                .from(userMissionChatRoom)
                .join(userMissionChatRoom.missionChatRoom, missionChatRoom)
                .join(mission)
                .on(missionChatRoom.missionId.eq(mission.id))
                .where(userIdEq(userId),
                        isJoined()
                ).fetch();
    }

    private BooleanBuilder userIdEq(Long userId) {
        return new BooleanBuilder(userMissionChatRoom.userId.eq(userId));
    }

    private BooleanBuilder isJoined() {
        return new BooleanBuilder(userMissionChatRoom.isJoined.eq(true));
    }
}
