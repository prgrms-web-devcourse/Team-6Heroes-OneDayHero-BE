package com.sixheroes.onedayheroapi.chatroom;

import com.sixheroes.onedayheroapi.chatroom.request.CreateMissionChatRoomRequest;
import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.chatroom.ChatRoomService;
import com.sixheroes.onedayheroapplication.chatroom.request.CreateMissionChatRoomServiceRequest;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomCreateResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomExitResponse;
import com.sixheroes.onedayheroapplication.chatroom.response.MissionChatRoomFindResponse;
import com.sixheroes.onedayherochat.application.ChatService;
import com.sixheroes.onedayherochat.application.response.ChatMessageApiResponse;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatRoomController.class)
public class ChatRoomControllerTest extends RestDocsSupport {

    @MockBean
    private ChatRoomService chatRoomService;

    @MockBean
    private ChatService chatService;

    @Override
    protected Object setController() {
        return new ChatRoomController(chatRoomService, chatService);
    }

    @DisplayName("유저는 미션에 대해 채팅방을 만들 수 있다.")
    @Test
    void createMissionChatRoom() throws Exception {
        // given
        var request = CreateMissionChatRoomRequest.builder()
                .missionId(1L)
                .userIds(List.of(1L, 2L))
                .build();

        var response = MissionChatRoomCreateResponse.builder()
                .id(1L)
                .missionId(request.missionId())
                .headCount(2)
                .build();

        given(chatRoomService.createChatRoom(any(CreateMissionChatRoomServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/chat-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                )
                .andDo(print())
                .andExpect(header().string("Location", "/api/v1/chat-rooms/" + response.id()))
                .andExpect(status().isCreated())
                .andDo(document("chatRoom-create",
                        requestHeaders(
                                headerWithName("Authorization").description("Auth Credential")
                        ),
                        requestFields(
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("userIds").type(JsonFieldType.ARRAY)
                                        .description("채팅방에 들어갈 유저 아이디")
                        ), responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data.headCount").type(JsonFieldType.NUMBER)
                                        .description("채팅방 인원 수"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )
                ))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.data.headCount").value(response.headCount()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 나가지 않은 채팅방을 조회 할 수 있다.")
    @Test
    void findChatroomWithJoined() throws Exception {
        // given
        var userId = 1L;
        var chatRoomAMissionId = 1L;
        var chatRoomAMissionStatus = "MATCHING";
        var chatRoomBMissionId = 2L;
        var chatRoomBMissionStatus = "MATCHING_COMPLETED";


        var response =
                List.of(
                        MissionChatRoomFindResponse.builder()
                                .id(1L)
                                .missionId(chatRoomAMissionId)
                                .title("심부름 해주실 분을 찾습니다.")
                                .missionStatus(chatRoomAMissionStatus)
                                .receiverId(1L)
                                .receiverNickname("슈퍼 히어로 토끼 A")
                                .receiverImagePath("s3://abc.jpeg")
                                .lastSentMessage("거의 다 와갑니다!")
                                .lastSentMessageTime(LocalDateTime.of(
                                        LocalDate.of(2023, 11, 12),
                                        LocalTime.NOON
                                ))
                                .headCount(1)
                                .build(),
                        MissionChatRoomFindResponse.builder()
                                .id(2L)
                                .missionId(chatRoomBMissionId)
                                .title("벌레 잡아주실 분을 찾습니다.")
                                .missionStatus(chatRoomBMissionStatus)
                                .receiverId(2L)
                                .receiverNickname("슈퍼 히어로 토끼 B")
                                .receiverImagePath("s3://abd.jpeg")
                                .lastSentMessage("어떤 벌레인가요?")
                                .lastSentMessageTime(LocalDateTime.of(
                                        LocalDate.of(2023, 11, 12),
                                        LocalTime.NOON
                                ))
                                .headCount(2)
                                .build()
                );

        given(chatRoomService.findJoinedChatRoom(any(Long.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/v1/chat-rooms/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatRoom-joined",
                        requestHeaders(
                                headerWithName("Authorization").description("Auth Credential")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                                        .description("채팅방 조회 배열"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("data[].missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data[].missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 상태"),
                                fieldWithPath("data[].receiverId").type(JsonFieldType.NUMBER)
                                        .description("수신자 아이디"),
                                fieldWithPath("data[].receiverNickname").type(JsonFieldType.STRING)
                                        .description("수신자 닉네임"),
                                fieldWithPath("data[].receiverImagePath").type(JsonFieldType.STRING)
                                        .description("수신자 프로필 이미지 경로"),
                                fieldWithPath("data[].lastSentMessage").type(JsonFieldType.STRING)
                                        .description("마지막으로 받은 메시지"),
                                fieldWithPath("data[].lastSentMessageTime").type(JsonFieldType.STRING)
                                        .description("마지막으로 받은 메시지 시간")
                                        .attributes(getDateTimeFormat()),
                                fieldWithPath("data[].headCount").type(JsonFieldType.NUMBER)
                                        .description("채팅방 인원"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )
                ))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].id").value(response.get(0).id()))
                .andExpect(jsonPath("$.data[0].missionId").value(response.get(0).missionId()))
                .andExpect(jsonPath("$.data[0].title").value(response.get(0).title()))
                .andExpect(jsonPath("$.data[0].missionStatus").value(response.get(0).missionStatus()))
                .andExpect(jsonPath("$.data[0].receiverNickname").value(response.get(0).receiverNickname()))
                .andExpect(jsonPath("$.data[0].receiverImagePath").value(response.get(0).receiverImagePath()))
                .andExpect(jsonPath("$.data[0].lastSentMessage").value(response.get(0).lastSentMessage()))
                .andExpect(jsonPath("$.data[0].lastSentMessageTime").value(DateTimeConverter.convertLocalDateTimeToString(response.get(0).lastSentMessageTime())))
                .andExpect(jsonPath("$.data[0].headCount").value(response.get(0).headCount()))
                .andExpect(jsonPath("$.data[1].id").value(response.get(1).id()))
                .andExpect(jsonPath("$.data[1].title").value(response.get(1).title()))
                .andExpect(jsonPath("$.data[1].receiverNickname").value(response.get(1).receiverNickname()))
                .andExpect(jsonPath("$.data[1].receiverImagePath").value(response.get(1).receiverImagePath()))
                .andExpect(jsonPath("$.data[1].lastSentMessage").value(response.get(1).lastSentMessage()))
                .andExpect(jsonPath("$.data[1].lastSentMessageTime").value(DateTimeConverter.convertLocalDateTimeToString(response.get(1).lastSentMessageTime())))
                .andExpect(jsonPath("$.data[1].headCount").value(response.get(1).headCount()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 채팅방에 있는 채팅 내역을 가져 올 수 있다.")
    @Test
    void findChatMessagesByChatRoomId() throws Exception {
        // given
        var chatRoomId = 1L;

        var chatMessageApiResponses = List.of(ChatMessageApiResponse.builder()
                        .senderNickName("거북이")
                        .message("안녕하세요!")
                        .sentMessageTime(LocalDateTime.of(
                                LocalDate.of(2023, 11, 26),
                                LocalTime.of(19, 25, 30)))
                        .build(),
                ChatMessageApiResponse.builder()
                        .senderNickName("두루미")
                        .message("안녕하세요! 미션 내용 확인하고자합니다!")
                        .sentMessageTime(LocalDateTime.of(
                                LocalDate.of(2023, 11, 26),
                                LocalTime.of(19, 27, 0)))
                        .build());


        given(chatService.findMessageByChatRoomId(any(Long.class)))
                .willReturn(chatMessageApiResponses);

        // when & then
        mockMvc.perform(get("/api/v1/chat-rooms/{chatRoomId}", chatRoomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatroom-find-messages",
                        requestHeaders(
                                headerWithName("Authorization").description("Auth Credential")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data[]").type(JsonFieldType.ARRAY)
                                        .description("채팅방 메시지 배열"),
                                fieldWithPath("data[].senderNickName").type(JsonFieldType.STRING)
                                        .description("채팅방 아이디"),
                                fieldWithPath("data[].message").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data[].sentMessageTime").type(JsonFieldType.STRING)
                                        .description("수신자 아이디")
                                        .attributes(getDateTimeFormat()),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].senderNickName").value(chatMessageApiResponses.get(0).senderNickName()))
                .andExpect(jsonPath("$.data[0].message").value(chatMessageApiResponses.get(0).message()))
                .andExpect(jsonPath("$.data[0].sentMessageTime").value(DateTimeConverter.convertLocalDateTimeToString(chatMessageApiResponses.get(0).sentMessageTime())))
                .andExpect(jsonPath("$.data[1].senderNickName").value(chatMessageApiResponses.get(1).senderNickName()))
                .andExpect(jsonPath("$.data[1].message").value(chatMessageApiResponses.get(1).message()))
                .andExpect(jsonPath("$.data[1].sentMessageTime").value(DateTimeConverter.convertLocalDateTimeToString(chatMessageApiResponses.get(1).sentMessageTime())))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 채팅방을 나갈 수 있다.")
    @Test
    void exitChatRoom() throws Exception {
        // given
        var chatRoomId = 1L;
        var userId = 1L;

        var response = MissionChatRoomExitResponse.builder()
                .id(chatRoomId)
                .userId(userId)
                .missionId(1L)
                .build();

        given(chatRoomService.exitChatRoom(any(Long.class), any(Long.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(patch("/api/v1/chat-rooms/{chatRoomId}/exit", chatRoomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("chatRoom-exit",
                        requestHeaders(
                                headerWithName("Authorization").description("Auth Credential")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디")
                        ), responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )
                ))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }
}
