package com.sixheroes.onedayheroapi.missionrequest;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestApproveRequest;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestCreateRequest;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestRejectRequest;
import com.sixheroes.onedayheroapplication.missionrequest.MissionRequestService;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestApproveServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestRejectServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestApproveResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestRejectResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionRequestController.class)
class MissionRequestControllerTest extends RestDocsSupport {

    @MockBean
    private MissionRequestService missionRequestService;

    @Override
    protected Object setController() {
        return new MissionRequestController(missionRequestService);
    }

    @DisplayName("미션 제안을 생성한다.")
    @Test
    void createMissionRequest() throws Exception {
        // given
        var userId = 1L;
        var missionId = 1L;
        var heroId = 1L;
        var missionRequestId = 1L;
        var missionStatus = "REQUEST";

        var missionRequestCreateRequest = new MissionRequestCreateRequest(userId, missionId, heroId);
        var missionRequestCreateResponse = new MissionRequestCreateResponse(missionRequestId, missionId, heroId, missionStatus);

        given(missionRequestService.createMissionRequest(any(MissionRequestCreateServiceRequest.class)))
            .willReturn(missionRequestCreateResponse);

        // when & then
        mockMvc.perform(post("/api/v1/mission-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missionRequestCreateRequest))
            ).andDo(print())
            .andExpect(header().string("Location", "/api/v1/mission-requests/" + missionRequestCreateResponse.missionRequestId()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.missionRequestId").value(missionRequestCreateResponse.missionRequestId()))
            .andExpect(jsonPath("$.data.missionId").value(missionRequestCreateResponse.missionId()))
            .andExpect(jsonPath("$.data.heroId").value(missionRequestCreateResponse.heroId()))
            .andExpect(jsonPath("$.data.missionRequestStatus").value(missionRequestCreateResponse.missionRequestStatus()))
            .andDo(document("mission-request-create",
                    requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER)
                            .description("유저 아이디"),
                        fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                            .description("미션 아이디"),
                        fieldWithPath("heroId").type(JsonFieldType.NUMBER)
                            .description("히어로 아이디")
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("HTTP 응답 코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                            .description("응답 데이터"),
                        fieldWithPath("data.missionRequestId").type(JsonFieldType.NUMBER)
                            .description("미션 요청 아이디"),
                        fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                            .description("미션 아이디"),
                        fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                            .description("히어로 아이디"),
                        fieldWithPath("data.missionRequestStatus").type(JsonFieldType.STRING)
                            .description("미션 요청 상태"),
                        fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                            .description("서버 응답 시간")
                            .attributes(getDateTimeFormat()
                    )
                )));
    }

    @DisplayName("미션 제안을 승낙한다.")
    @Test
    void approveMissionRequest() throws Exception {
        // given
        var missionRequestId = 1L;
        var missionId = 1L;
        var heroId = 1L;
        var missionStatus = "APPROVE";

        var missionRequestApproveRequest = new MissionRequestApproveRequest(heroId);
        var missionRequestApproveResponse = new MissionRequestApproveResponse(missionRequestId, missionId, heroId, missionStatus);

        given(missionRequestService.approveMissionRequest(anyLong(), any(MissionRequestApproveServiceRequest.class)))
            .willReturn(missionRequestApproveResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/mission-requests/{missionRequestId}/approve", missionRequestId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missionRequestApproveRequest))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.missionRequestId").value(missionRequestApproveResponse.missionRequestId()))
            .andExpect(jsonPath("$.data.missionId").value(missionRequestApproveResponse.missionId()))
            .andExpect(jsonPath("$.data.heroId").value(missionRequestApproveResponse.heroId()))
            .andExpect(jsonPath("$.data.missionRequestStatus").value(missionRequestApproveResponse.missionRequestStatus()))
            .andDo(document("mission-request-approve",
                pathParameters(
                    parameterWithName("missionRequestId").description("미션 제안 아이디")
                ),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER)
                        .description("유저 아이디")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.missionRequestId").type(JsonFieldType.NUMBER)
                        .description("미션 요청 아이디"),
                    fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                        .description("미션 아이디"),
                    fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                        .description("히어로 아이디"),
                    fieldWithPath("data.missionRequestStatus").type(JsonFieldType.STRING)
                        .description("미션 요청 상태"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()
                        )
                )));
    }

    @DisplayName("미션 제안을 거절한다.")
    @Test
    void rejectMissionRequest() throws Exception {
        // given
        var missionRequestId = 1L;
        var missionId = 1L;
        var heroId = 1L;
        var missionStatus = "REJECT";

        var missionRequestRejectRequest = new MissionRequestRejectRequest(heroId);
        var missionRequestRejectResponse = new MissionRequestRejectResponse(missionRequestId, missionId, heroId, missionStatus);

        given(missionRequestService.rejectMissionRequest(anyLong(), any(MissionRequestRejectServiceRequest.class)))
            .willReturn(missionRequestRejectResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/mission-requests/{missionRequestId}/reject", missionRequestId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missionRequestRejectRequest))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.missionRequestId").value(missionRequestRejectResponse.missionRequestId()))
            .andExpect(jsonPath("$.data.missionId").value(missionRequestRejectResponse.missionId()))
            .andExpect(jsonPath("$.data.heroId").value(missionRequestRejectResponse.heroId()))
            .andExpect(jsonPath("$.data.missionRequestStatus").value(missionRequestRejectResponse.missionRequestStatus()))
            .andDo(document("mission-request-reject",
                pathParameters(
                    parameterWithName("missionRequestId").description("미션 제안 아이디")
                ),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER)
                        .description("유저 아이디")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.missionRequestId").type(JsonFieldType.NUMBER)
                        .description("미션 요청 아이디"),
                    fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                        .description("미션 아이디"),
                    fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                        .description("히어로 아이디"),
                    fieldWithPath("data.missionRequestStatus").type(JsonFieldType.STRING)
                        .description("미션 요청 상태"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()
                        )
                )));
    }
}
