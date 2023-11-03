package com.sixheroes.onedayheroapi.missionrequest;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestApproveRequest;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestCreateRequest;
import com.sixheroes.onedayheroapi.missionrequest.request.MissionRequestRejectRequest;
import com.sixheroes.onedayheroapplication.missionrequest.MissionRequestService;
import com.sixheroes.onedayheroapplication.missionrequest.dto.*;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestApproveServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestCreateServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.request.MissionRequestRejectServiceRequest;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestApproveResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestCreateResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestRejectResponse;
import com.sixheroes.onedayheroapplication.missionrequest.response.MissionRequestResponse;
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

@WebMvcTest(MissionRequestController.class)
class MissionRequestControllerTest extends RestDocsSupport {

    @MockBean
    private MissionRequestService missionRequestService;

    @Override
    protected Object setController() {
        return new MissionRequestController(missionRequestService);
    }

    @DisplayName("미션 요청을 생성한다.")
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

    @DisplayName("제안받은 미션을 조회한다.")
    @Test
    void findMissionRequest() throws Exception {
        var time = LocalDateTime.now();
        var mission1 = createMissionDto(1L, "MATCHING", time);
        var missionRequest1 = createMissionRequestDto(1L, mission1);

        var mission2 = createMissionDto(2L, "MATCHING", time.minusDays(1));
        var missionRequest2 = createMissionRequestDto(2L, mission2);

        var mission3 = createMissionDto(3L, "MATCHING_COMPLETED", time.minusDays(3));
        var missionRequest3 = createMissionRequestDto(3L, mission3);

        var mission4 = createMissionDto(4L, "MISSION_COMPLETED", time.minusDays(2));
        var missionRequest4 = createMissionRequestDto(4L, mission4);

        var mission5 = createMissionDto(5L, "EXPIRED", time.minusDays(3));
        var missionRequest5 = createMissionRequestDto(5L, mission5);

        var missionRequests = List.of(missionRequest1, missionRequest2, missionRequest3, missionRequest4, missionRequest5);
        var pageNumber = 0;
        var pageSize = 5;
        var page = PageRequest.of(0, 5);
        var slice = new SliceImpl(missionRequests, page, true);

        when(missionRequestService.findMissionRequest(anyLong(), any(Pageable.class))).thenReturn(new MissionRequestResponse(slice));

        mockMvc.perform(get("/api/v1/mission-requests")
                .param("heroId", "1")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.missionRequests.content[0].missionRequestId").value(missionRequest1.missionRequestId()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionId").value(missionRequest1.mission().missionId()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionStatus").value(missionRequest1.mission().missionStatus()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.bookmarkCount").value(missionRequest1.mission().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionCreatedAt").value(DateTimeConverter.convertLocalDateTimeToString(missionRequest1.mission().missionCreatedAt())))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.region.si").value(missionRequest1.mission().region().si()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.region.gu").value(missionRequest1.mission().region().gu()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.region.dong").value(missionRequest1.mission().region().dong()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionCategory.missionCategoryCode").value(missionRequest1.mission().missionCategory().missionCategoryCode()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionCategory.categoryName").value(missionRequest1.mission().missionCategory().categoryName()))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionRequest1.mission().missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionRequest1.mission().missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionRequest1.mission().missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionRequests.content[0].mission.missionInfo.price").value(missionRequest1.mission().missionInfo().price()))
                .andExpect(jsonPath("$.data.missionRequests.pageable.pageNumber").value(pageNumber))
                .andExpect(jsonPath("$.data.missionRequests.pageable.pageSize").value(pageSize))
                .andExpect(jsonPath("$.data.missionRequests.pageable.sort.empty").value(true))
                .andExpect(jsonPath("$.data.missionRequests.pageable.offset").value(0))
                .andExpect(jsonPath("$.data.missionRequests.pageable.paged").value(true))
                .andExpect(jsonPath("$.data.missionRequests.pageable.unpaged").value(false))
                .andExpect(jsonPath("$.data.missionRequests.size").value(pageSize))
                .andExpect(jsonPath("$.data.missionRequests.number").value(0))
                .andExpect(jsonPath("$.data.missionRequests.sort.empty").value(true))
                .andExpect(jsonPath("$.data.missionRequests.sort.sorted").value(false))
                .andExpect(jsonPath("$.data.missionRequests.sort.unsorted").value(true))
                .andExpect(jsonPath("$.data.missionRequests.numberOfElements").value(missionRequests.size()))
                .andExpect(jsonPath("$.data.missionRequests.first").value(true))
                .andExpect(jsonPath("$.data.missionRequests.last").value(false))
                .andExpect(jsonPath("$.data.missionRequests.empty").value(false))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-request-find", queryParameters(
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
                                fieldWithPath("data.missionRequests").type(JsonFieldType.OBJECT)
                                        .description("미션 제안 데이터"),
                                fieldWithPath("data.missionRequests.content[]").type(JsonFieldType.ARRAY)
                                        .description("미션 제안 데이터 배열"),
                                fieldWithPath("data.missionRequests.content[].missionRequestId").type(JsonFieldType.NUMBER)
                                        .description("미션 제안 ID"),
                                fieldWithPath("data.missionRequests.content[].mission").type(JsonFieldType.OBJECT)
                                        .description("미션 정보"),
                                fieldWithPath("data.missionRequests.content[].mission.missionId").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.missionRequests.content[].mission.missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 상태"),
                                fieldWithPath("data.missionRequests.content[].mission.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 찜 카운트"),
                                fieldWithPath("data.missionRequests.content[].mission.missionCreatedAt")
                                        .attributes(getDateTimeFormat())
                                        .type(JsonFieldType.STRING)
                                        .description("미션 생성일"),
                                fieldWithPath("data.missionRequests.content[].mission.region").type(JsonFieldType.OBJECT)
                                        .description("지역"),
                                fieldWithPath("data.missionRequests.content[].mission.region.si").type(JsonFieldType.STRING)
                                        .description("시"),
                                fieldWithPath("data.missionRequests.content[].mission.region.gu").type(JsonFieldType.STRING)
                                        .description("구"),
                                fieldWithPath("data.missionRequests.content[].mission.region.dong").type(JsonFieldType.STRING)
                                        .description("동"),
                                fieldWithPath("data.missionRequests.content[].mission.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리"),
                                fieldWithPath("data.missionRequests.content[].mission.missionCategory.missionCategoryCode").type(JsonFieldType.STRING)
                                        .description("카테고리 코드"),
                                fieldWithPath("data.missionRequests.content[].mission.missionCategory.categoryName").type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("data.missionRequests.content[].mission.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보"),
                                fieldWithPath("data.missionRequests.content[].mission.missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .attributes(getDateFormat())
                                        .description("미션 시작 일"),
                                fieldWithPath("data.missionRequests.content[].mission.missionInfo.startTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 시작 시간"),
                                fieldWithPath("data.missionRequests.content[].mission.missionInfo.endTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 종료 시간"),
                                fieldWithPath("data.missionRequests.content[].mission.missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("급여"),
                                fieldWithPath("data.missionRequests.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionRequests.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.missionRequests.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.missionRequests.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionRequests.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionRequests.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionRequests.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.missionRequests.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.missionRequests.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.missionRequests.size").type(JsonFieldType.NUMBER)
                                        .description("미션 제안 리스트 크기"),
                                fieldWithPath("data.missionRequests.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionRequests.sort").type(JsonFieldType.OBJECT)
                                        .description("미션 제안 리스트 정렬 정보 객체"),
                                fieldWithPath("data.missionRequests.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 제안 리스트의 정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionRequests.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 제안 리스트의 정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionRequests.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 제안 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionRequests.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.missionRequests.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.missionRequests.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.missionRequests.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 제안 리스트가 비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                )));

    }

    private MissionRequestDto createMissionRequestDto(
            Long missionRequestId,
            MissionDto missionDto
    ) {
       return new MissionRequestDto(missionRequestId, missionDto);
    }

    private MissionDto createMissionDto(
            Long missionId,
            String missionStatus,
            LocalDateTime missionCreatedAt
    ) {
        return MissionDto.builder()
                .missionId(1L)
                .missionStatus(missionStatus)
                .missionCreatedAt(missionCreatedAt)
                .bookmarkCount(5)
                .missionCategory(createMissionCategoryDto())
                .missionInfo(createMissionInfoDto())
                .region(createRegionDto())
                .build();
    }

    private RegionDto createRegionDto() {
        return RegionDto.builder()
                .si("서울시")
                .gu("프로구")
                .dong("래머동")
                .build();
    }

    private MissionInfoDto createMissionInfoDto() {
        return MissionInfoDto.builder()
                .missionDate(LocalDate.of(2023, 10, 30))
                .startTime(LocalTime.of(12, 0, 0))
                .endTime(LocalTime.of(18, 0, 0))
                .price(30000)
                .build();
    }

    private MissionCategoryDto createMissionCategoryDto() {
        return MissionCategoryDto.builder()
                .missionCategoryCode("MC_001")
                .categoryName("서빙")
                .build();
    }
}
