package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.mission.request.*;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionFindFilterServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.*;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionController.class)
public class MissionControllerTest extends RestDocsSupport {

    @MockBean
    private MissionService missionService;

    @Override
    protected Object setController() {
        return new MissionController(missionService);
    }

    @DisplayName("유저는 미션을 만들 수 있다.")
    @Test
    void createMission() throws Exception {
        // given
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(
                missionDate,
                startTime
        );

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateRequest = createMissionCreateRequest(missionInfoRequest);

        var regionResponse = createRegionResponse();
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(regionResponse, missionCategoryResponse, missionCreateRequest, missionInfoResponse);

        given(missionService.createMission(any(MissionCreateServiceRequest.class), any(LocalDateTime.class)))
                .willReturn(missionResponse);

        // when & then
        mockMvc.perform(post("/api/v1/missions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missionCreateRequest))
                )
                .andDo(print())
                .andExpect(header().string("Location", "/api/v1/missions/" + missionResponse.id()))
                .andExpect(status().isCreated())
                .andDo(document("mission-create",
                        requestFields(
                                fieldWithPath("missionCategoryId").type(JsonFieldType.NUMBER)
                                        .description("카테고리 아이디"),
                                fieldWithPath("citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("regionId").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.region").type(JsonFieldType.OBJECT)
                                        .description("미션 수행 지역 객체"),
                                fieldWithPath("data.region.id").type(JsonFieldType.NUMBER)
                                        .description("미션 수행 지역 아이디"),
                                fieldWithPath("data.region.si").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 시"),
                                fieldWithPath("data.region.gu").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 구"),
                                fieldWithPath("data.region.dong").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 동"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("data.missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("data.missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("data.missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 찜 개수"),
                                fieldWithPath("data.missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 진행 상태 (MATCHING)"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(missionResponse.id()))
                .andExpect(jsonPath("$.data.missionCategory").exists())
                .andExpect(jsonPath("$.data.missionCategory.id").value(missionCategoryResponse.id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.region").exists())
                .andExpect(jsonPath("$.data.region.id").value(regionResponse.id()))
                .andExpect(jsonPath("$.data.region.si").value(regionResponse.si()))
                .andExpect(jsonPath("$.data.region.gu").value(regionResponse.gu()))
                .andExpect(jsonPath("$.data.region.dong").value(regionResponse.dong()))
                .andExpect(jsonPath("$.data.longitude").value(missionResponse.longitude()))
                .andExpect(jsonPath("$.data.latitude").value(missionResponse.latitude()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.title").value(missionInfoResponse.title()))
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertLocalDateTimeToString(missionInfoResponse.deadlineTime())))
                .andExpect(jsonPath("$.data.missionInfo.price").value(missionInfoResponse.price()))
                .andExpect(jsonPath("$.data.bookmarkCount").value(missionResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionStatus").value(missionResponse.missionStatus()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 미션을 삭제 할 수 있다.")
    @Test
    void deleteMission() throws Exception {
        // given
        var missionId = 1L;
        var citizenId = 1L;

        var request = MissionDeleteRequest.builder()
                .citizenId(citizenId)
                .build();


        willDoNothing().given(missionService).deleteMission(any(Long.class), any(Long.class));

        // when & then
        mockMvc.perform(delete("/api/v1/missions/{missionId}", missionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("mission-delete",
                        pathParameters(
                                parameterWithName("missionId").description("미션 아이디")
                        ),
                        requestFields(
                                fieldWithPath("citizenId").description("시민 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 미션을 수정 할 수 있다.")
    @Test
    void updateMission() throws Exception {

        // given
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(
                missionDate,
                startTime
        );

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionUpdateRequest = createMissionUpdateRequest(missionInfoRequest);

        var regionResponse = createRegionResponse();
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(regionResponse, missionCategoryResponse, missionUpdateRequest, missionInfoResponse);

        given(missionService.updateMission(any(Long.class), any(MissionUpdateServiceRequest.class), any(LocalDateTime.class)))
                .willReturn(missionResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/missions/{missionId}", missionResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missionUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-update",
                        pathParameters(
                                parameterWithName("missionId").description("미션 아이디")
                        ),
                        requestFields(
                                fieldWithPath("missionCategoryId").type(JsonFieldType.NUMBER)
                                        .description("카테고리 아이디"),
                                fieldWithPath("citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("regionId").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("missionInfo.title").type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.region").type(JsonFieldType.OBJECT)
                                        .description("미션 수행 지역 객체"),
                                fieldWithPath("data.region.id").type(JsonFieldType.NUMBER)
                                        .description("미션 수행 지역 아이디"),
                                fieldWithPath("data.region.si").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 시"),
                                fieldWithPath("data.region.gu").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 구"),
                                fieldWithPath("data.region.dong").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 동"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("data.missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("data.missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("data.missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 찜 개수"),
                                fieldWithPath("data.missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 진행 상태 (MATCHING)"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.missionCategory").exists())
                .andExpect(jsonPath("$.data.missionCategory.id").value(missionCategoryResponse.id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.region").exists())
                .andExpect(jsonPath("$.data.region.id").value(regionResponse.id()))
                .andExpect(jsonPath("$.data.region.si").value(regionResponse.si()))
                .andExpect(jsonPath("$.data.region.gu").value(regionResponse.gu()))
                .andExpect(jsonPath("$.data.region.dong").value(regionResponse.dong()))
                .andExpect(jsonPath("$.data.longitude").value(missionResponse.longitude()))
                .andExpect(jsonPath("$.data.latitude").value(missionResponse.latitude()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.title").value(missionInfoResponse.title()))
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertLocalDateTimeToString(missionInfoResponse.deadlineTime())))
                .andExpect(jsonPath("$.data.missionInfo.price").value(missionInfoResponse.price()))
                .andExpect(jsonPath("$.data.bookmarkCount").value(missionResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionStatus").value(missionResponse.missionStatus()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 미션을 연장 할 수 있다.")
    @Test
    void postponeMission() throws Exception {

        // given
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(
                missionDate,
                LocalTime.of(10, 0)
        );

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionUpdateRequest = createMissionUpdateRequest(missionInfoRequest);

        var regionResponse = createRegionResponse();
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(regionResponse, missionCategoryResponse, missionUpdateRequest, missionInfoResponse);

        given(missionService.extendMission(any(Long.class), any(MissionUpdateServiceRequest.class), any(LocalDateTime.class)))
                .willReturn(missionResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/missions/{missionId}/extend", missionResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missionUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-extend",
                        pathParameters(
                                parameterWithName("missionId").description("미션 아이디")
                        ),
                        requestFields(
                                fieldWithPath("missionCategoryId").type(JsonFieldType.NUMBER)
                                        .description("카테고리 아이디"),
                                fieldWithPath("citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("regionId").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.region").type(JsonFieldType.OBJECT)
                                        .description("미션 수행 지역 객체"),
                                fieldWithPath("data.region.id").type(JsonFieldType.NUMBER)
                                        .description("미션 수행 지역 아이디"),
                                fieldWithPath("data.region.si").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 시"),
                                fieldWithPath("data.region.gu").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 구"),
                                fieldWithPath("data.region.dong").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 동"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("data.missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("data.missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("data.missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 찜 개수"),
                                fieldWithPath("data.missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 진행 상태 (MATCHING)"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.missionCategory").exists())
                .andExpect(jsonPath("$.data.missionCategory.id").value(missionCategoryResponse.id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.region").exists())
                .andExpect(jsonPath("$.data.region.id").value(regionResponse.id()))
                .andExpect(jsonPath("$.data.region.si").value(regionResponse.si()))
                .andExpect(jsonPath("$.data.region.gu").value(regionResponse.gu()))
                .andExpect(jsonPath("$.data.region.dong").value(regionResponse.dong()))
                .andExpect(jsonPath("$.data.longitude").value(missionResponse.longitude()))
                .andExpect(jsonPath("$.data.latitude").value(missionResponse.latitude()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.title").value(missionInfoResponse.title()))
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertLocalDateTimeToString(missionInfoResponse.deadlineTime())))
                .andExpect(jsonPath("$.data.missionInfo.price").value(missionInfoResponse.price()))
                .andExpect(jsonPath("$.data.bookmarkCount").value(missionResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionStatus").value(missionResponse.missionStatus()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 미션을 완료 상태로 변경 할 수 있다.")
    @Test
    void completeMission() throws Exception {

        // given
        var missionId = 1L;
        var userId = 1L;

        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(
                missionDate,
                startTime
        );

        var missionCompleteRequest = MissionCompleteRequest.builder()
                .userId(userId)
                .build();

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);

        var regionResponse = createRegionResponse();
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createCompleteMissionResponse(missionId, regionResponse, missionCategoryResponse, missionInfoResponse);
        given(missionService.completeMission(any(Long.class), any(Long.class)))
                .willReturn(missionResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/missions/{missionId}/complete", missionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missionCompleteRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-complete",
                        pathParameters(
                                parameterWithName("missionId").description("미션 아이디")
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
                                        .description("미션 아이디"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.region").type(JsonFieldType.OBJECT)
                                        .description("미션 수행 지역 객체"),
                                fieldWithPath("data.region.id").type(JsonFieldType.NUMBER)
                                        .description("미션 수행 지역 아이디"),
                                fieldWithPath("data.region.si").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 시"),
                                fieldWithPath("data.region.gu").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 구"),
                                fieldWithPath("data.region.dong").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 동"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("data.missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("data.missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("data.missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 찜 개수"),
                                fieldWithPath("data.missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 진행 상태 (MATCHING)"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.missionCategory").exists())
                .andExpect(jsonPath("$.data.missionCategory.id").value(missionCategoryResponse.id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.region").exists())
                .andExpect(jsonPath("$.data.region.id").value(regionResponse.id()))
                .andExpect(jsonPath("$.data.region.si").value(regionResponse.si()))
                .andExpect(jsonPath("$.data.region.gu").value(regionResponse.gu()))
                .andExpect(jsonPath("$.data.region.dong").value(regionResponse.dong()))
                .andExpect(jsonPath("$.data.longitude").value(missionResponse.longitude()))
                .andExpect(jsonPath("$.data.latitude").value(missionResponse.latitude()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.title").value(missionInfoResponse.title()))
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertLocalDateTimeToString(missionInfoResponse.deadlineTime())))
                .andExpect(jsonPath("$.data.missionInfo.price").value(missionInfoResponse.price()))
                .andExpect(jsonPath("$.data.bookmarkCount").value(missionResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionStatus").value(missionResponse.missionStatus()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 하나의 미션을 조회 할 수 있다.")
    @Test
    void findOneMission() throws Exception {

        // given
        var missionDate = LocalDate.of(2023, 10, 10);
        var startTime = LocalTime.of(10, 0);
        var endTime = LocalTime.of(10, 30);
        var deadlineTime = LocalDateTime.of(
                missionDate,
                LocalTime.of(9, 30)
        );

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionUpdateRequest = createMissionUpdateRequest(missionInfoRequest);

        var regionResponse = createRegionResponse();
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(regionResponse, missionCategoryResponse, missionUpdateRequest, missionInfoResponse);

        given(missionService.findOne(any(Long.class)))
                .willReturn(missionResponse);

        // when & then
        mockMvc.perform(get("/api/v1/missions/{missionId}", missionResponse.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missionUpdateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-findOne",
                        pathParameters(
                                parameterWithName("missionId").description("미션 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("미션 아이디"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.region").type(JsonFieldType.OBJECT)
                                        .description("미션 수행 지역 객체"),
                                fieldWithPath("data.region.id").type(JsonFieldType.NUMBER)
                                        .description("미션 수행 지역 아이디"),
                                fieldWithPath("data.region.si").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 시"),
                                fieldWithPath("data.region.gu").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 구"),
                                fieldWithPath("data.region.dong").type(JsonFieldType.STRING)
                                        .description("미션 수행 지역 동"),
                                fieldWithPath("data.longitude").type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("data.latitude").type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("data.missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("data.missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 상세 내용"),
                                fieldWithPath("data.missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .description("미션 마감 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 포상금"),
                                fieldWithPath("data.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 찜 개수"),
                                fieldWithPath("data.missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 진행 상태 (MATCHING)"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.missionCategory").exists())
                .andExpect(jsonPath("$.data.missionCategory.id").value(missionCategoryResponse.id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.region").exists())
                .andExpect(jsonPath("$.data.region.id").value(regionResponse.id()))
                .andExpect(jsonPath("$.data.region.si").value(regionResponse.si()))
                .andExpect(jsonPath("$.data.region.gu").value(regionResponse.gu()))
                .andExpect(jsonPath("$.data.region.dong").value(regionResponse.dong()))
                .andExpect(jsonPath("$.data.longitude").value(missionResponse.longitude()))
                .andExpect(jsonPath("$.data.latitude").value(missionResponse.latitude()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.title").value(missionInfoResponse.title()))
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertLocalDateTimeToString(missionInfoResponse.deadlineTime())))
                .andExpect(jsonPath("$.data.missionInfo.price").value(missionInfoResponse.price()))
                .andExpect(jsonPath("$.data.bookmarkCount").value(missionResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionStatus").value(missionResponse.missionStatus()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 현재 진행 중인 미션을 조회 할 수 있다.")
    @Test
    void findProgressMissionByUserId() throws Exception {
        // given
        var citizenId = 1L;
        var pageRequest = PageRequest.of(0, 4);
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionProgressResponse = createMissionProgressResponse(missionCategoryResponse);

        var sliceMissionResponses = new SliceImpl<>(List.of(missionProgressResponse), pageRequest, false);
        var result = MissionProgressResponses.builder()
                .missionProgressResponses(sliceMissionResponses)
                .build();

        given(missionService.findProgressMission(any(Pageable.class), any(Long.class)))
                .willReturn(result);

        // when & then
        mockMvc.perform(get("/api/v1/missions/progress/{userId}", citizenId)
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-progress-find",
                        pathParameters(
                                parameterWithName("userId")
                                        .description("시민 아이디")
                        ),
                        queryParameters(
                                parameterWithName("page").optional()
                                        .description("페이지 번호"),
                                parameterWithName("size").optional()
                                        .description("데이터 크기"),
                                parameterWithName("sort").optional()
                                        .description("정렬 기준 필드")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.missionProgressResponses").type(JsonFieldType.OBJECT)
                                        .description("미션 응답 데이터"),
                                fieldWithPath("data.missionProgressResponses.content[]").type(JsonFieldType.ARRAY)
                                        .description("미션 응답 데이터 배열"),
                                fieldWithPath("data.missionProgressResponses.content[].id").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.missionProgressResponses.content[].title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.missionProgressResponses.content[].missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 객체"),
                                fieldWithPath("data.missionProgressResponses.content[].missionCategory.id")
                                        .description("미션 카테고리 ID"),
                                fieldWithPath("data.missionProgressResponses.content[].missionCategory.code")
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionProgressResponses.content[].missionCategory.name")
                                        .description("미션 카테고리 이름"),
                                fieldWithPath("data.missionProgressResponses.content[].missionDate").type(JsonFieldType.STRING)
                                        .attributes(getDateFormat())
                                        .description("미션 날짜"),
                                fieldWithPath("data.missionProgressResponses.content[].bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("북마크 횟수"),
                                fieldWithPath("data.missionProgressResponses.content[].missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 상태"),
                                fieldWithPath("data.missionProgressResponses.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionProgressResponses.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.missionProgressResponses.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.missionProgressResponses.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionProgressResponses.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionProgressResponses.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionProgressResponses.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.missionProgressResponses.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.missionProgressResponses.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.missionProgressResponses.size").type(JsonFieldType.NUMBER)
                                        .description("미션 리스트 크기"),
                                fieldWithPath("data.missionProgressResponses.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionProgressResponses.sort").type(JsonFieldType.OBJECT)
                                        .description("미션 리스트 정렬 정보 객체"),
                                fieldWithPath("data.missionProgressResponses.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionProgressResponses.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionProgressResponses.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionProgressResponses.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.missionProgressResponses.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.missionProgressResponses.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.missionProgressResponses.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트가 비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )
                ))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].id").value(missionProgressResponse.id()))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].title").value(missionProgressResponse.title()))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].missionCategory.id").value(missionProgressResponse.missionCategory().id()))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].missionCategory.code").value(missionProgressResponse.missionCategory().code()))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].missionCategory.name").value(missionProgressResponse.missionCategory().name()))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].missionDate").value(DateTimeConverter.convertDateToString(missionProgressResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].bookmarkCount").value(missionProgressResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionProgressResponses.content[0].missionStatus").value(missionProgressResponse.missionStatus()))
                .andExpect(jsonPath("$.data.missionProgressResponses.pageable.pageNumber").value(result.missionProgressResponses().getPageable().getPageNumber()))
                .andExpect(jsonPath("$.data.missionProgressResponses.pageable.pageSize").value(result.missionProgressResponses().getPageable().getPageSize()))
                .andExpect(jsonPath("$.data.missionProgressResponses.pageable.sort.empty").value(result.missionProgressResponses().getPageable().getSort().isEmpty()))
                .andExpect(jsonPath("$.data.missionProgressResponses.pageable.offset").value(result.missionProgressResponses().getPageable().getOffset()))
                .andExpect(jsonPath("$.data.missionProgressResponses.pageable.paged").value(result.missionProgressResponses().getPageable().isPaged()))
                .andExpect(jsonPath("$.data.missionProgressResponses.pageable.unpaged").value(result.missionProgressResponses().getPageable().isUnpaged()))
                .andExpect(jsonPath("$.data.missionProgressResponses.size").value(result.missionProgressResponses().getSize()))
                .andExpect(jsonPath("$.data.missionProgressResponses.number").value(result.missionProgressResponses().getNumber()))
                .andExpect(jsonPath("$.data.missionProgressResponses.sort.empty").value(result.missionProgressResponses().getSort().isEmpty()))
                .andExpect(jsonPath("$.data.missionProgressResponses.sort.sorted").value(result.missionProgressResponses().getSort().isSorted()))
                .andExpect(jsonPath("$.data.missionProgressResponses.sort.unsorted").value(result.missionProgressResponses().getSort().isUnsorted()))
                .andExpect(jsonPath("$.data.missionProgressResponses.numberOfElements").value(result.missionProgressResponses().getNumberOfElements()))
                .andExpect(jsonPath("$.data.missionProgressResponses.first").value(result.missionProgressResponses().isFirst()))
                .andExpect(jsonPath("$.data.missionProgressResponses.last").value(result.missionProgressResponses().isLast()))
                .andExpect(jsonPath("$.data.missionProgressResponses.empty").value(result.missionProgressResponses().isEmpty()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저는 필터에 따라 미션들을 조회 할 수 있다.")
    @Test
    void findAllByDynamicCondition() throws Exception {
        // given
        var pageRequest = PageRequest.of(0, 4);
        var request = MissionFindFilterRequest.builder()
                .missionCategoryCodes(List.of("MC_001", "MC_002"))
                .missionDates(List.of(LocalDate.of(2023, 10, 31), LocalDate.of(2023, 11, 2)))
                .regionIds(List.of(1L, 3L))
                .build();

        var regionResponse = createRegionResponse();
        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponseA = createMissionInfoResponse(LocalDate.of(2023, 10, 21));
        var missionInfoResponseB = createMissionInfoResponse(LocalDate.of(2023, 10, 22));

        var missionResponseA = createMissionResponse(1L, regionResponse, missionCategoryResponse, missionInfoResponseA);
        var missionResponseB = createMissionResponse(2L, regionResponse, missionCategoryResponse, missionInfoResponseB);
        var missionResponseList = List.of(missionResponseA, missionResponseB);

        var sliceMissionResponses = new SliceImpl<>(missionResponseList, pageRequest, false);
        var missionResponses = new MissionResponses(sliceMissionResponses);

        given(missionService.findAllByDynamicCondition(any(Pageable.class), any(MissionFindFilterServiceRequest.class)))
                .willReturn(missionResponses);

        // when & then
        mockMvc.perform(get("/api/v1/missions")
                        .param("page", "0")
                        .param("size", "4")
                        .param("sort", "")
                        .param("missionCategoryCodes", "MC_001", "MC_002")
                        .param("missionDates", "2023-10-31", "2023-11-02")
                        .param("regionIds", "1", "3")
                        .flashAttr("request", request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("mission-find-DynamicCondition", queryParameters(
                                parameterWithName("page").optional()
                                        .description("페이지 번호"),
                                parameterWithName("size").optional()
                                        .description("데이터 크기"),
                                parameterWithName("sort").optional()
                                        .description("정렬 기준 필드"),
                                parameterWithName("missionCategoryCodes").optional()
                                        .description("미션 카테고리 필터 코드"),
                                parameterWithName("missionDates").optional()
                                        .description("미션 수행 일 필터"),
                                parameterWithName("regionIds").optional()
                                        .description("미션 수행 지역 필터")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.missionResponses").type(JsonFieldType.OBJECT)
                                        .description("미션 응답 데이터"),
                                fieldWithPath("data.missionResponses.content[]").type(JsonFieldType.ARRAY)
                                        .description("미션 응답 데이터 배열"),
                                fieldWithPath("data.missionResponses.content[].id").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.missionResponses.content[].missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 객체"),
                                fieldWithPath("data.missionResponses.content[].missionCategory.id")
                                        .description("미션 카테고리 ID"),
                                fieldWithPath("data.missionResponses.content[].missionCategory.code")
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionResponses.content[].missionCategory.name")
                                        .description("미션 카테고리 이름"),
                                fieldWithPath("data.missionResponses.content[].citizenId")
                                        .description("시민 ID"),
                                fieldWithPath("data.missionResponses.content[].region").type(JsonFieldType.OBJECT)
                                        .description("지역 정보 객체"),
                                fieldWithPath("data.missionResponses.content[].region.id")
                                        .description("지역 ID"),
                                fieldWithPath("data.missionResponses.content[].region.si")
                                        .description("지역 시"),
                                fieldWithPath("data.missionResponses.content[].region.gu")
                                        .description("지역 구"),
                                fieldWithPath("data.missionResponses.content[].region.dong")
                                        .description("지역 동"),
                                fieldWithPath("data.missionResponses.content[].longitude")
                                        .description("경도"),
                                fieldWithPath("data.missionResponses.content[].latitude")
                                        .description("위도"),
                                fieldWithPath("data.missionResponses.content[].missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 상세 제목"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.content").type(JsonFieldType.STRING)
                                        .description("미션 내용"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .attributes(getDateFormat())
                                        .description("미션 날짜"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.startTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 시작 시간"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.endTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 종료 시간"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.deadlineTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 마감 시간"),
                                fieldWithPath("data.missionResponses.content[].missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 가격"),
                                fieldWithPath("data.missionResponses.content[].bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("북마크 횟수"),
                                fieldWithPath("data.missionResponses.content[].missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 상태"),
                                fieldWithPath("data.missionResponses.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionResponses.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.missionResponses.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.missionResponses.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionResponses.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionResponses.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionResponses.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.missionResponses.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.missionResponses.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.missionResponses.size").type(JsonFieldType.NUMBER)
                                        .description("미션 리스트 크기"),
                                fieldWithPath("data.missionResponses.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionResponses.sort").type(JsonFieldType.OBJECT)
                                        .description("미션 리스트 정렬 정보 객체"),
                                fieldWithPath("data.missionResponses.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionResponses.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionResponses.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionResponses.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.missionResponses.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.missionResponses.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.missionResponses.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트가 비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )
                ))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.missionResponses.content[0].id").value(missionResponseA.id()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionCategory.id").value(missionResponseA.missionCategory().id()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionCategory.code").value(missionResponseA.missionCategory().code()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionCategory.name").value(missionResponseA.missionCategory().name()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].citizenId").value(missionResponseA.citizenId()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].region.id").value(missionResponseA.region().id()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].region.si").value(missionResponseA.region().si()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].region.gu").value(missionResponseA.region().gu()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].region.dong").value(missionResponseA.region().dong()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].longitude").value(missionResponseA.longitude()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].latitude").value(missionResponseA.latitude()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.title").value(missionResponseA.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.content").value(missionResponseA.missionInfo().content()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionResponseA.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionResponseA.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionResponseA.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.deadlineTime").value(DateTimeConverter.convertLocalDateTimeToString(missionResponseA.missionInfo().deadlineTime())))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionInfo.price").value(missionResponseA.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].bookmarkCount").value(missionResponseA.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionResponses.content[0].missionStatus").value(missionResponseA.missionStatus()))
                .andExpect(jsonPath("$.data.missionResponses.pageable.pageNumber").value(missionResponses.missionResponses().getPageable().getPageNumber()))
                .andExpect(jsonPath("$.data.missionResponses.pageable.pageSize").value(missionResponses.missionResponses().getPageable().getPageSize()))
                .andExpect(jsonPath("$.data.missionResponses.pageable.sort.empty").value(missionResponses.missionResponses().getPageable().getSort().isEmpty()))
                .andExpect(jsonPath("$.data.missionResponses.pageable.offset").value(missionResponses.missionResponses().getPageable().getOffset()))
                .andExpect(jsonPath("$.data.missionResponses.pageable.paged").value(missionResponses.missionResponses().getPageable().isPaged()))
                .andExpect(jsonPath("$.data.missionResponses.pageable.unpaged").value(missionResponses.missionResponses().getPageable().isUnpaged()))
                .andExpect(jsonPath("$.data.missionResponses.size").value(missionResponses.missionResponses().getSize()))
                .andExpect(jsonPath("$.data.missionResponses.number").value(missionResponses.missionResponses().getNumber()))
                .andExpect(jsonPath("$.data.missionResponses.sort.empty").value(missionResponses.missionResponses().getSort().isEmpty()))
                .andExpect(jsonPath("$.data.missionResponses.sort.sorted").value(missionResponses.missionResponses().getSort().isSorted()))
                .andExpect(jsonPath("$.data.missionResponses.sort.unsorted").value(missionResponses.missionResponses().getSort().isUnsorted()))
                .andExpect(jsonPath("$.data.missionResponses.numberOfElements").value(missionResponses.missionResponses().getNumberOfElements()))
                .andExpect(jsonPath("$.data.missionResponses.first").value(missionResponses.missionResponses().isFirst()))
                .andExpect(jsonPath("$.data.missionResponses.last").value(missionResponses.missionResponses().isLast()))
                .andExpect(jsonPath("$.data.missionResponses.empty").value(missionResponses.missionResponses().isEmpty()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    private MissionResponse createMissionResponse(
            Long id,
            RegionResponse regionResponse,
            MissionCategoryResponse missionCategoryResponse,
            MissionResponse.MissionInfoResponse missionInfoResponse
    ) {
        return MissionResponse.builder()
                .id(id)
                .citizenId(1L)
                .missionCategory(missionCategoryResponse)
                .missionInfo(missionInfoResponse)
                .bookmarkCount(0)
                .region(regionResponse)
                .longitude(123.45)
                .latitude(123.45)
                .missionStatus("MATCHING")
                .build();
    }

    private MissionResponse createMissionResponse(
            RegionResponse regionResponse,
            MissionCategoryResponse missionCategoryResponse,
            MissionCreateRequest missionCreateRequest,
            MissionResponse.MissionInfoResponse missionInfoResponse
    ) {
        return MissionResponse.builder()
                .id(1L)
                .missionCategory(missionCategoryResponse)
                .citizenId(missionCreateRequest.citizenId())
                .region(regionResponse)
                .longitude(missionCreateRequest.longitude())
                .latitude(missionCreateRequest.latitude())
                .missionInfo(missionInfoResponse)
                .bookmarkCount(0)
                .missionStatus("MATCHING")
                .build();
    }

    private MissionResponse.MissionInfoResponse createMissionInfoResponse(
            LocalDate missionDate
    ) {
        return MissionResponse.MissionInfoResponse.builder()
                .title("제목")
                .content("내용")
                .missionDate(missionDate)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 30))
                .deadlineTime(LocalDateTime.of(
                        missionDate,
                        LocalTime.of(8, 30)
                ))
                .price(1000)
                .build();
    }

    private MissionResponse createMissionResponse(
            RegionResponse regionResponse,
            MissionCategoryResponse missionCategoryResponse,
            MissionUpdateRequest missionUpdateRequest,
            MissionResponse.MissionInfoResponse missionInfoResponse
    ) {
        return MissionResponse.builder()
                .id(1L)
                .missionCategory(missionCategoryResponse)
                .citizenId(missionUpdateRequest.citizenId())
                .region(regionResponse)
                .longitude(missionUpdateRequest.longitude())
                .latitude(missionUpdateRequest.latitude())
                .missionInfo(missionInfoResponse)
                .bookmarkCount(0)
                .missionStatus("MATCHING")
                .build();
    }

    private MissionResponse createCompleteMissionResponse(
            Long id,
            RegionResponse regionResponse,
            MissionCategoryResponse missionCategoryResponse,
            MissionResponse.MissionInfoResponse missionInfoResponse
    ) {
        return MissionResponse.builder()
                .id(id)
                .citizenId(1L)
                .missionCategory(missionCategoryResponse)
                .missionInfo(missionInfoResponse)
                .bookmarkCount(0)
                .region(regionResponse)
                .longitude(123.45)
                .latitude(123.45)
                .missionStatus("MISSION_COMPLETED")
                .build();
    }

    private RegionResponse createRegionResponse() {
        return RegionResponse.builder()
                .id(1L)
                .si("서울시")
                .gu("강남구")
                .dong("역삼동")
                .build();
    }

    private MissionCategoryResponse createMissionCategoryResponse() {
        return MissionCategoryResponse.builder()
                .id(1L)
                .code("MC_001")
                .name("서빙")
                .build();
    }

    private MissionResponse.MissionInfoResponse createMissionInfoResponse(
            MissionInfoRequest missionInfoRequest
    ) {
        return MissionResponse.MissionInfoResponse.builder()
                .title(missionInfoRequest.title())
                .content(missionInfoRequest.content())
                .missionDate(missionInfoRequest.missionDate())
                .startTime(missionInfoRequest.startTime())
                .endTime(missionInfoRequest.endTime())
                .deadlineTime(missionInfoRequest.deadlineTime())
                .price(missionInfoRequest.price())
                .build();
    }

    private MissionCreateRequest createMissionCreateRequest(
            MissionInfoRequest missionInfoRequest
    ) {
        return MissionCreateRequest.builder()
                .missionCategoryId(1L)
                .citizenId(1L)
                .regionId(1L)
                .latitude(1234252.23)
                .longitude(1234277.388)
                .missionInfo(missionInfoRequest)
                .build();
    }

    private MissionUpdateRequest createMissionUpdateRequest(
            MissionInfoRequest missionInfoRequest
    ) {
        return MissionUpdateRequest.builder()
                .missionCategoryId(1L)
                .citizenId(1L)
                .regionId(1L)
                .latitude(1234252.23)
                .longitude(1234277.388)
                .missionInfo(missionInfoRequest)
                .build();
    }

    private MissionProgressResponse createMissionProgressResponse(
            MissionCategoryResponse missionCategory
    ) {
        return MissionProgressResponse.builder()
                .id(1L)
                .title("제목")
                .missionCategory(missionCategory)
                .missionDate(LocalDate.of(2023, 11, 6))
                .bookmarkCount(1)
                .missionStatus("MATCHING")
                .build();
    }

    private MissionInfoRequest createMissionInfoRequest(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDateTime deadlineTime
    ) {
        return MissionInfoRequest
                .builder()
                .title("제목")
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
                .build();
    }
}
