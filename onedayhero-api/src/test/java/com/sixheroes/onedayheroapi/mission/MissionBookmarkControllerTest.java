package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        var response = createMissionBookmarkResponse();
        given(missionBookmarkService.createMissionBookmark(anyLong(), any(MissionBookmarkCreateServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-bookmark-create",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                        ),
                        requestFields(
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("생성된 미션 찜 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(Attributes.key("format").value("yyyy-MM-dd'T'HH:mm:ss"))
                        )
                ));
    }

    @DisplayName("유저는 찜한 미션을 취소할 수 있다.")
    @Test
    void cancelMissionBookmark() throws Exception {
        // given
        var request = createMissionBookmarkCancelApplicationRequest();
        willDoNothing().given(missionBookmarkService).cancelMissionBookmark(anyLong(), any(MissionBookmarkCancelServiceRequest.class));

        // when & then
        mockMvc.perform(delete("/api/v1/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-bookmark-cancel",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                        ),
                        requestFields(
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디")
                        )
                ));
    }

    private MissionBookmarkCreateServiceRequest createMissionBookmarkCreateApplicationRequest() {
        return MissionBookmarkCreateServiceRequest.builder()
                .missionId(1L)
                .build();
    }

    private MissionBookmarkResponse createMissionBookmarkResponse() {
        return MissionBookmarkResponse.builder()
                .id(1L)
                .build();
    }

    private MissionBookmarkCancelServiceRequest createMissionBookmarkCancelApplicationRequest() {
        return MissionBookmarkCancelServiceRequest.builder()
                .missionId(1L)
                .build();
    }
}
