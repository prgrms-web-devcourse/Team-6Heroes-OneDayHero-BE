package com.sixheroes.onedayheroapi.missionmatch;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.missionmatch.MissionMatchService;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchGiveUpServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.request.MissionMatchWithdrawServiceRequest;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchCreateResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchGiveUpResponse;
import com.sixheroes.onedayheroapplication.missionmatch.response.MissionMatchWithdrawResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MissionMatchController.class)
class MissionMatchControllerTest extends RestDocsSupport {

    @MockBean
    private MissionMatchService missionMatchService;

    @Override
    protected Object setController() {
        return new MissionMatchController(missionMatchService);
    }

    @DisplayName("미션이 매칭 중 상태일 때 매칭 완료 상태로 설정할 수 있다.")
    @Test
    void createMissionMatch() throws Exception {
        // given
        var request = createMissionMatchCreateServiceRequest();
        var response = createMissionMatchResponse();

        given(missionMatchService.createMissionMatch(any(MissionMatchCreateServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/mission-matches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.data.heroId").value(response.heroId()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-match-create",
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
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("생성된 미션 매칭 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("매칭이 완료된 미션 아이디"),
                                fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                                        .description("미션에 매칭된 히어로 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(getDateTimeFormat())
                        )
                ));
    }

    @DisplayName("시민은 매칭완료 상태인 본인의 미션에 대한 미션매칭을 철회할 수 있다.")
    @Test
    void withdrawMissionMatch() throws Exception {
        var request = createMissionMatchWithdrawServiceRequest();
        var response = createMissionMatchWithdrawResponse();

        given(missionMatchService.withdrawMissionMatch(any(MissionMatchWithdrawServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/mission-matches/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.citizenId").value(response.citizenId()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-match-withdraw",
                        requestFields(
                                fieldWithPath("citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("미션매칭 아이디"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("매칭완료를 철회한 시민 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("매칭완료가 취소된 미션 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(getDateTimeFormat())
                        )
                ));
    }

    @DisplayName("히어로는 본인이 매칭된 미션에 대한 미션매칭을 철회할 수 있다.")
    @Test
    void giveUpMissionMatch() throws Exception {
        var request = createMissionMatchGiveUpServiceRequest();
        var response = createMissionMatchGiveUpResponse();

        given(missionMatchService.giveUpMissionMatch(any(MissionMatchGiveUpServiceRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/mission-matches/give-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.heroId").value(response.heroId()))
                .andExpect(jsonPath("$.data.missionId").value(response.missionId()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-match-giveUp",
                        requestFields(
                                fieldWithPath("heroId").type(JsonFieldType.NUMBER)
                                        .description("히어로 아이디"),
                                fieldWithPath("missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("미션매칭 아이디"),
                                fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                                        .description("매칭완료를 포기한 히어로 아이디"),
                                fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                                        .description("매칭완료가 취소된 미션 아이디"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(getDateTimeFormat())
                        )
                ));
    }

    private MissionMatchCreateServiceRequest createMissionMatchCreateServiceRequest() {
        return MissionMatchCreateServiceRequest.builder()
                .userId(1L)
                .missionId(2L)
                .heroId(3L)
                .build();
    }

    private MissionMatchWithdrawServiceRequest createMissionMatchWithdrawServiceRequest() {
        return MissionMatchWithdrawServiceRequest.builder()
                .citizenId(1L)
                .missionId(2L)
                .build();
    }

    private MissionMatchGiveUpServiceRequest createMissionMatchGiveUpServiceRequest() {
        return MissionMatchGiveUpServiceRequest.builder()
                .heroId(3L)
                .missionId(2L)
                .build();
    }


    private MissionMatchCreateResponse createMissionMatchResponse() {
        return MissionMatchCreateResponse.builder()
                .id(1L)
                .missionId(2L)
                .heroId(3L)
                .build();
    }

    private MissionMatchWithdrawResponse createMissionMatchWithdrawResponse() {
        return MissionMatchWithdrawResponse.builder()
                .id(1L)
                .missionId(2L)
                .citizenId(1L)
                .build();
    }

    private MissionMatchGiveUpResponse createMissionMatchGiveUpResponse() {
        return MissionMatchGiveUpResponse.builder()
                .id(1L)
                .missionId(2L)
                .heroId(3L)
                .build();
    }
}
