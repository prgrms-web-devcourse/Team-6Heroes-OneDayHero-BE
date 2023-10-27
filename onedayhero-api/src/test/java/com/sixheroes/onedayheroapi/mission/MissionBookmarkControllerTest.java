package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCancelResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MissionBookmarkController.class)
class MissionBookmarkControllerTest extends RestDocsSupport {

    @MockBean
    private MissionBookmarkService missionBookmarkService;

    @Override
    protected Object setController() {
        return new MissionBookmarkController(missionBookmarkService);
    }

    @DisplayName("유저는 미션을 찜 할 수 있다.")
    @Test
    void createMissionBookmark() throws Exception {
        // given
        var request = createMissionBookmarkCreateApplicationRequest();
        var response = createMissionBookmarkCreateResponse();
        given(missionBookmarkService.createMissionBookmark(any(MissionBookmarkCreateServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andDo(print())
                .andExpect(header().string("Location", "/api/v1/missions/" + request.missionId()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-bookmark-create",
                        requestFields(
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("생성된 북마크 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("찜한 미션 아이디"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("찜한 유저 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(Attributes.key("format").value("yyyy-MM-dd'T'HH:mm:ss"))
                        )
                ));
    }

    @DisplayName("유저는 찜한 미션을 취소할 수 있다.")
    @Test
    void cancelMissionBookmark() throws Exception {
        // given
        var request = createMissionBookmarkCancelApplicationRequest();
        var response = createMissionBookmarkCancelResponse();
        given(missionBookmarkService.cancelMissionBookmark(any(MissionBookmarkCancelServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(delete("/api/v1/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-bookmark-cancel",
                        requestFields(
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                        .description("유저 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("제거된 북마크 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("찜 취소된 미션 아이디"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("찜 취소한 유저 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(Attributes.key("format").value("yyyy-MM-dd'T'HH:mm:ss"))
                        )
                ));
    }

    private MissionBookmarkCreateServiceRequest createMissionBookmarkCreateApplicationRequest() {
        return MissionBookmarkCreateServiceRequest.builder()
                .missionId(1L)
                .userId(10L)
                .build();
    }

    private MissionBookmarkCreateResponse createMissionBookmarkCreateResponse() {
        return MissionBookmarkCreateResponse.builder()
                .id(1L)
                .missionId(1L)
                .userId(10L)
                .build();
    }

    private MissionBookmarkCancelServiceRequest createMissionBookmarkCancelApplicationRequest() {
        return MissionBookmarkCancelServiceRequest.builder()
                .missionId(1L)
                .userId(10L)
                .build();
    }

    private MissionBookmarkCancelResponse createMissionBookmarkCancelResponse() {
        return MissionBookmarkCancelResponse.builder()
                .id(1L)
                .missionId(1L)
                .userId(10L)
                .build();
    }
}
