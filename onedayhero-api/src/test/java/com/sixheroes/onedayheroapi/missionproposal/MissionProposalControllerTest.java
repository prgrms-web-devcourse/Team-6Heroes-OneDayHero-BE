package com.sixheroes.onedayheroapi.missionproposal;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalApproveRequest;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalCreateRequest;
import com.sixheroes.onedayheroapi.missionproposal.request.MissionProposalRejectRequest;
import com.sixheroes.onedayheroapplication.missionproposal.MissionProposalService;
import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalApproveServiceRequest;
import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionproposal.request.MissionProposalRejectServiceRequest;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalApproveResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalCreateResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalRejectResponse;
import com.sixheroes.onedayheroapplication.missionproposal.response.MissionProposalResponses;
import com.sixheroes.onedayheroapplication.missionproposal.response.dto.*;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionProposalController.class)
class MissionProposalControllerTest extends RestDocsSupport {

    @MockBean
    private MissionProposalService missionProposalService;

    @Override
    protected Object setController() {
        return new MissionProposalController(missionProposalService);
    }

    @DisplayName("미션 요청을 생성한다.")
    @Test
    void createMissionProposal() throws Exception {
        // given
        var userId = 1L;
        var missionId = 1L;
        var heroId = 1L;
        var missionProposalId = 1L;
        var missionStatus = "PROPOSAL";

        var missionProposalCreateRequest = new MissionProposalCreateRequest(userId, missionId, heroId);
        var missionProposalCreateResponse = new MissionProposalCreateResponse(missionProposalId, missionId, heroId, missionStatus);

        given(missionProposalService.createMissionProposal(any(MissionProposalCreateServiceRequest.class)))
            .willReturn(missionProposalCreateResponse);

        // when & then
        mockMvc.perform(post("/api/v1/mission-proposals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missionProposalCreateRequest))
            ).andDo(print())
            .andExpect(header().string("Location", "/api/v1/mission-proposals/" + missionProposalCreateResponse.id()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(missionProposalCreateResponse.id()))
            .andExpect(jsonPath("$.data.missionId").value(missionProposalCreateResponse.missionId()))
            .andExpect(jsonPath("$.data.heroId").value(missionProposalCreateResponse.heroId()))
            .andExpect(jsonPath("$.data.missionProposalStatus").value(missionProposalCreateResponse.missionProposalStatus()))
            .andDo(document("mission-proposal-create",
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
                            .description("미션 요청 아이디"),
                        fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                            .description("미션 아이디"),
                        fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                            .description("히어로 아이디"),
                        fieldWithPath("data.missionProposalStatus").type(JsonFieldType.STRING)
                            .description("미션 요청 상태"),
                        fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                            .description("서버 응답 시간")
                            .attributes(getDateTimeFormat()
                    )
                )));
    }

    @DisplayName("미션 제안을 승낙한다.")
    @Test
    void approveMissionProposal() throws Exception {
        // given
        var missionProposalId = 1L;
        var missionId = 1L;
        var heroId = 1L;
        var missionStatus = "APPROVE";

        var missionProposalApproveRequest = new MissionProposalApproveRequest(heroId);
        var missionProposalApproveResponse = new MissionProposalApproveResponse(missionProposalId, missionId, heroId, missionStatus);

        given(missionProposalService.approveMissionProposal(anyLong(), any(MissionProposalApproveServiceRequest.class)))
            .willReturn(missionProposalApproveResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/mission-proposals/{missionProposalId}/approve", missionProposalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missionProposalApproveRequest))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(missionProposalApproveResponse.id()))
            .andExpect(jsonPath("$.data.missionId").value(missionProposalApproveResponse.missionId()))
            .andExpect(jsonPath("$.data.heroId").value(missionProposalApproveResponse.heroId()))
            .andExpect(jsonPath("$.data.missionProposalStatus").value(missionProposalApproveResponse.missionProposalStatus()))
            .andDo(document("mission-proposal-approve",
                pathParameters(
                    parameterWithName("missionProposalId").description("미션 제안 아이디")
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
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("미션 요청 아이디"),
                    fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                        .description("미션 아이디"),
                    fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                        .description("히어로 아이디"),
                    fieldWithPath("data.missionProposalStatus").type(JsonFieldType.STRING)
                        .description("미션 요청 상태"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()
                        )
                )));
    }

    @DisplayName("미션 제안을 거절한다.")
    @Test
    void rejectMissionProposal() throws Exception {
        // given
        var missionProposalId = 1L;
        var missionId = 1L;
        var heroId = 1L;
        var missionStatus = "REJECT";

        var missionProposalRejectRequest = new MissionProposalRejectRequest(heroId);
        var missionProposalRejectResponse = new MissionProposalRejectResponse(missionProposalId, missionId, heroId, missionStatus);

        given(missionProposalService.rejectMissionProposal(anyLong(), any(MissionProposalRejectServiceRequest.class)))
            .willReturn(missionProposalRejectResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/mission-proposals/{missionProposalId}/reject", missionProposalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missionProposalRejectRequest))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(missionProposalRejectResponse.id()))
            .andExpect(jsonPath("$.data.missionId").value(missionProposalRejectResponse.missionId()))
            .andExpect(jsonPath("$.data.heroId").value(missionProposalRejectResponse.heroId()))
            .andExpect(jsonPath("$.data.missionProposalStatus").value(missionProposalRejectResponse.missionProposalStatus()))
            .andDo(document("mission-proposal-reject",
                pathParameters(
                    parameterWithName("missionProposalId").description("미션 제안 아이디")
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
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("미션 요청 아이디"),
                    fieldWithPath("data.missionId").type(JsonFieldType.NUMBER)
                        .description("미션 아이디"),
                    fieldWithPath("data.heroId").type(JsonFieldType.NUMBER)
                        .description("히어로 아이디"),
                    fieldWithPath("data.missionProposalStatus").type(JsonFieldType.STRING)
                        .description("미션 요청 상태"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()
                        )
                )));
    }

    @DisplayName("제안받은 미션을 조회한다.")
    @Test
    void findMissionProposal() throws Exception {
        var time = LocalDateTime.now();
        var mission1 = createMissionDto(1L, "MATCHING", time);
        var missionProposal1 = createMissionProposalDto(1L, mission1);

        var mission2 = createMissionDto(2L, "MATCHING", time.minusDays(1));
        var missionProposal2 = createMissionProposalDto(2L, mission2);

        var mission3 = createMissionDto(3L, "MATCHING_COMPLETED", time.minusDays(3));
        var missionProposal3 = createMissionProposalDto(3L, mission3);

        var mission4 = createMissionDto(4L, "MISSION_COMPLETED", time.minusDays(2));
        var missionProposal4 = createMissionProposalDto(4L, mission4);

        var mission5 = createMissionDto(5L, "EXPIRED", time.minusDays(3));
        var missionProposal5 = createMissionProposalDto(5L, mission5);

        var missionProposals = List.of(missionProposal1, missionProposal2, missionProposal3, missionProposal4, missionProposal5);
        var pageNumber = 0;
        var pageSize = 5;
        var page = PageRequest.of(0, 5);
        var slice = new SliceImpl(missionProposals, page, true);

        when(missionProposalService.findMissionProposal(anyLong(), any(Pageable.class))).thenReturn(new MissionProposalResponses(slice));

        mockMvc.perform(get("/api/v1/mission-proposals")
                .param("heroId", "1")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.missionProposals.content[0].id").value(missionProposal1.id()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.bookmarkCount").value(missionProposal1.mission().bookmarkCount()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.status").value(missionProposal1.mission().status()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.id").value(missionProposal1.mission().id()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.createdAt").value(DateTimeConverter.convertLocalDateTimeToString(missionProposal1.mission().createdAt())))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.region.si").value(missionProposal1.mission().region().si()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.region.gu").value(missionProposal1.mission().region().gu()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.region.dong").value(missionProposal1.mission().region().dong()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionCategory.code").value(missionProposal1.mission().missionCategory().code()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionCategory.name").value(missionProposal1.mission().missionCategory().name()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionInfo.title").value(missionProposal1.mission().missionInfo().title()))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionProposal1.mission().missionInfo().missionDate())))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionProposal1.mission().missionInfo().startTime())))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionProposal1.mission().missionInfo().endTime())))
            .andExpect(jsonPath("$.data.missionProposals.content[0].mission.missionInfo.price").value(missionProposal1.mission().missionInfo().price()))
            .andExpect(jsonPath("$.data.missionProposals.pageable.pageNumber").value(pageNumber))
            .andExpect(jsonPath("$.data.missionProposals.pageable.pageSize").value(pageSize))
            .andExpect(jsonPath("$.data.missionProposals.pageable.sort.empty").value(true))
            .andExpect(jsonPath("$.data.missionProposals.pageable.offset").value(0))
            .andExpect(jsonPath("$.data.missionProposals.pageable.paged").value(true))
            .andExpect(jsonPath("$.data.missionProposals.pageable.unpaged").value(false))
            .andExpect(jsonPath("$.data.missionProposals.size").value(pageSize))
            .andExpect(jsonPath("$.data.missionProposals.number").value(0))
            .andExpect(jsonPath("$.data.missionProposals.sort.empty").value(true))
            .andExpect(jsonPath("$.data.missionProposals.sort.sorted").value(false))
            .andExpect(jsonPath("$.data.missionProposals.sort.unsorted").value(true))
            .andExpect(jsonPath("$.data.missionProposals.numberOfElements").value(missionProposals.size()))
            .andExpect(jsonPath("$.data.missionProposals.first").value(true))
            .andExpect(jsonPath("$.data.missionProposals.last").value(false))
            .andExpect(jsonPath("$.data.missionProposals.empty").value(false))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andDo(document("mission-proposal-find", queryParameters(
                    parameterWithName("page").optional()
                        .description("페이지 번호"),
                    parameterWithName("size").optional()
                        .description("데이터 크기"),
                    parameterWithName("heroId").optional()
                        .description("히어로 아이디")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.missionProposals").type(JsonFieldType.OBJECT)
                        .description("미션 제안 데이터"),
                    fieldWithPath("data.missionProposals.content[]").type(JsonFieldType.ARRAY)
                        .description("미션 제안 데이터 배열"),
                    fieldWithPath("data.missionProposals.content[].id").type(JsonFieldType.NUMBER)
                        .description("미션 제안 ID"),
                    fieldWithPath("data.missionProposals.content[].mission").type(JsonFieldType.OBJECT)
                        .description("미션 정보"),
                    fieldWithPath("data.missionProposals.content[].mission.id").type(JsonFieldType.NUMBER)
                        .description("미션 ID"),
                    fieldWithPath("data.missionProposals.content[].mission.status").type(JsonFieldType.STRING)
                        .description("미션 상태"),
                    fieldWithPath("data.missionProposals.content[].mission.bookmarkCount").type(JsonFieldType.NUMBER)
                        .description("미션 찜 카운트"),
                    fieldWithPath("data.missionProposals.content[].mission.createdAt")
                        .attributes(getDateTimeFormat())
                        .type(JsonFieldType.STRING)
                        .description("미션 생성일"),
                    fieldWithPath("data.missionProposals.content[].mission.region").type(JsonFieldType.OBJECT)
                        .description("지역"),
                    fieldWithPath("data.missionProposals.content[].mission.region.si").type(JsonFieldType.STRING)
                        .description("시"),
                    fieldWithPath("data.missionProposals.content[].mission.region.gu").type(JsonFieldType.STRING)
                        .description("구"),
                    fieldWithPath("data.missionProposals.content[].mission.region.dong").type(JsonFieldType.STRING)
                        .description("동"),
                    fieldWithPath("data.missionProposals.content[].mission.missionCategory").type(JsonFieldType.OBJECT)
                        .description("미션 카테고리"),
                    fieldWithPath("data.missionProposals.content[].mission.missionCategory.code").type(JsonFieldType.STRING)
                        .description("카테고리 코드"),
                    fieldWithPath("data.missionProposals.content[].mission.missionCategory.name").type(JsonFieldType.STRING)
                        .description("카테고리 이름"),
                    fieldWithPath("data.missionProposals.content[].mission.missionInfo").type(JsonFieldType.OBJECT)
                        .description("미션 상세 정보"),
                    fieldWithPath("data.missionProposals.content[].mission.missionInfo.title").type(JsonFieldType.STRING)
                        .description("미션 제목"),
                    fieldWithPath("data.missionProposals.content[].mission.missionInfo.missionDate").type(JsonFieldType.STRING)
                        .attributes(getDateFormat())
                        .description("미션 시작 일"),
                    fieldWithPath("data.missionProposals.content[].mission.missionInfo.startTime").type(JsonFieldType.STRING)
                        .attributes(getTimeFormat())
                        .description("미션 시작 시간"),
                    fieldWithPath("data.missionProposals.content[].mission.missionInfo.endTime").type(JsonFieldType.STRING)
                        .attributes(getTimeFormat())
                        .description("미션 종료 시간"),
                    fieldWithPath("data.missionProposals.content[].mission.missionInfo.price").type(JsonFieldType.NUMBER)
                        .description("급여"),
                    fieldWithPath("data.missionProposals.pageable.pageNumber").type(JsonFieldType.NUMBER)
                        .description("현재 페이지 번호"),
                    fieldWithPath("data.missionProposals.pageable.pageSize").type(JsonFieldType.NUMBER)
                        .description("페이지 크기"),
                    fieldWithPath("data.missionProposals.pageable.sort").type(JsonFieldType.OBJECT)
                        .description("정렬 상태 객체"),
                    fieldWithPath("data.missionProposals.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                        .description("정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.missionProposals.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                        .description("정렬 정보가 있는지 여부"),
                    fieldWithPath("data.missionProposals.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                        .description("정렬 정보가 정렬되지 않은지 여부"),
                    fieldWithPath("data.missionProposals.pageable.offset").type(JsonFieldType.NUMBER)
                        .description("페이지 번호"),
                    fieldWithPath("data.missionProposals.pageable.paged").type(JsonFieldType.BOOLEAN)
                        .description("페이징이 되어 있는지 여부"),
                    fieldWithPath("data.missionProposals.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                        .description("페이징이 되어 있지 않은지 여부"),
                    fieldWithPath("data.missionProposals.size").type(JsonFieldType.NUMBER)
                        .description("미션 제안 리스트 크기"),
                    fieldWithPath("data.missionProposals.number").type(JsonFieldType.NUMBER)
                        .description("현재 페이지 번호"),
                    fieldWithPath("data.missionProposals.sort").type(JsonFieldType.OBJECT)
                        .description("미션 제안 리스트 정렬 정보 객체"),
                    fieldWithPath("data.missionProposals.sort.empty").type(JsonFieldType.BOOLEAN)
                        .description("미션 제안 리스트의 정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.missionProposals.sort.sorted").type(JsonFieldType.BOOLEAN)
                        .description("미션 제안 리스트의 정렬 정보가 있는지 여부"),
                    fieldWithPath("data.missionProposals.sort.unsorted").type(JsonFieldType.BOOLEAN)
                        .description("미션 제안 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                    fieldWithPath("data.missionProposals.numberOfElements").type(JsonFieldType.NUMBER)
                        .description("현재 페이지의 요소 수"),
                    fieldWithPath("data.missionProposals.first").type(JsonFieldType.BOOLEAN)
                        .description("첫 번째 페이지인지 여부"),
                    fieldWithPath("data.missionProposals.last").type(JsonFieldType.BOOLEAN)
                        .description("마지막 페이지인지 여부"),
                    fieldWithPath("data.missionProposals.empty").type(JsonFieldType.BOOLEAN)
                        .description("미션 제안 리스트가 비어있는지 여부"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .attributes(getDateTimeFormat())
                        .description("서버 응답 시간")
                )));

    }

    private MissionProposalResponse createMissionProposalDto(
            Long missionProposalId,
            MissionForMissionProposalResponse missionDto
    ) {
       return new MissionProposalResponse(missionProposalId, missionDto);
    }

    private MissionForMissionProposalResponse createMissionDto(
        Long missionId,
        String missionStatus,
        LocalDateTime missionCreatedAt
    ) {
        return MissionForMissionProposalResponse.builder()
            .id(missionId)
            .status(missionStatus)
            .createdAt(missionCreatedAt)
            .bookmarkCount(5)
            .missionCategory(createMissionCategoryDto())
            .missionInfo(createMissionInfoDto())
            .region(createRegionDto())
            .build();
    }

    private RegionForMissionProposalResponse createRegionDto() {
        return RegionForMissionProposalResponse.builder()
            .si("서울시")
            .gu("프로구")
            .dong("래머동")
            .build();
    }

    private MissionInfoForMissionProposalResponse createMissionInfoDto() {
        return MissionInfoForMissionProposalResponse.builder()
            .title("미션 제목")
            .missionDate(LocalDate.of(2023, 10, 30))
            .startTime(LocalTime.of(12, 0, 0))
            .endTime(LocalTime.of(18, 0, 0))
            .price(30000)
            .build();
    }

    private MissionCategoryForMissionProposalResponse createMissionCategoryDto() {
        return MissionCategoryForMissionProposalResponse.builder()
            .code("MC_001")
            .name("서빙")
            .build();
    }
}
