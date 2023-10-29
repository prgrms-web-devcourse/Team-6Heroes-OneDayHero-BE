package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.mission.request.MissionCreateRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionDeleteRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionInfoRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionUpdateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionUpdateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
        var deadlineTime = LocalTime.of(10, 0);

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionCreateRequest = createMissionCreateRequest(missionInfoRequest);

        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(missionCategoryResponse, missionCreateRequest, missionInfoResponse);

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
                                        .description("생성된 미션 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.regionId").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("data.missionCategory.categoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.location").type(JsonFieldType.OBJECT)
                                        .description("위도, 경도 정보 객체"),
                                fieldWithPath("data.location.x").type(JsonFieldType.NUMBER)
                                        .description("경도 (longitude)"),
                                fieldWithPath("data.location.y").type(JsonFieldType.NUMBER)
                                        .description("위도 (latitude)"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
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
                .andExpect(jsonPath("$.data.missionCategory.categoryId").value(missionCategoryResponse.categoryId()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.location").exists())
                .andExpect(jsonPath("$.data.location.x").value(missionResponse.location().getX()))
                .andExpect(jsonPath("$.data.location.y").value(missionResponse.location().getY()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.deadlineTime())))
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
        var deadlineTime = LocalTime.of(10, 0);

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionUpdateRequest = createMissionUpdateRequest(missionInfoRequest);

        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(missionCategoryResponse, missionUpdateRequest, missionInfoResponse);

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
                                        .description("생성된 미션 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.regionId").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("data.missionCategory.categoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.location").type(JsonFieldType.OBJECT)
                                        .description("위도, 경도 정보 객체"),
                                fieldWithPath("data.location.x").type(JsonFieldType.NUMBER)
                                        .description("경도 (longitude)"),
                                fieldWithPath("data.location.y").type(JsonFieldType.NUMBER)
                                        .description("위도 (latitude)"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
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
                .andExpect(jsonPath("$.data.missionCategory.categoryId").value(missionCategoryResponse.categoryId()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.location").exists())
                .andExpect(jsonPath("$.data.location.x").value(missionResponse.location().getX()))
                .andExpect(jsonPath("$.data.location.y").value(missionResponse.location().getY()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.deadlineTime())))
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
        var deadlineTime = LocalTime.of(10, 0);

        var missionInfoRequest = createMissionInfoRequest(missionDate, startTime, endTime, deadlineTime);
        var missionUpdateRequest = createMissionUpdateRequest(missionInfoRequest);

        var missionCategoryResponse = createMissionCategoryResponse();
        var missionInfoResponse = createMissionInfoResponse(missionInfoRequest);
        var missionResponse = createMissionResponse(missionCategoryResponse, missionUpdateRequest, missionInfoResponse);

        given(missionService.extendMission(any(MissionUpdateServiceRequest.class), any(LocalDateTime.class)))
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
                                        .description("생성된 미션 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.citizenId").type(JsonFieldType.NUMBER)
                                        .description("시민 아이디"),
                                fieldWithPath("data.regionId").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("data.missionCategory.categoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.location").type(JsonFieldType.OBJECT)
                                        .description("위도, 경도 정보 객체"),
                                fieldWithPath("data.location.x").type(JsonFieldType.NUMBER)
                                        .description("경도 (longitude)"),
                                fieldWithPath("data.location.y").type(JsonFieldType.NUMBER)
                                        .description("위도 (latitude)"),
                                fieldWithPath("data.missionInfo").type(JsonFieldType.OBJECT)
                                        .description("미션 상세 정보 객체"),
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
                .andExpect(jsonPath("$.data.missionCategory.categoryId").value(missionCategoryResponse.categoryId()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(missionCategoryResponse.code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(missionCategoryResponse.name()))
                .andExpect(jsonPath("$.data.location").exists())
                .andExpect(jsonPath("$.data.location.x").value(missionResponse.location().getX()))
                .andExpect(jsonPath("$.data.location.y").value(missionResponse.location().getY()))
                .andExpect(jsonPath("$.data.missionInfo").exists())
                .andExpect(jsonPath("$.data.missionInfo.content").value(missionInfoResponse.content()))
                .andExpect(jsonPath("$.data.missionInfo.missionDate").value(DateTimeConverter.convertDateToString(missionInfoResponse.missionDate())))
                .andExpect(jsonPath("$.data.missionInfo.startTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.startTime())))
                .andExpect(jsonPath("$.data.missionInfo.endTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.endTime())))
                .andExpect(jsonPath("$.data.missionInfo.deadlineTime").value(DateTimeConverter.convertTimetoString(missionInfoResponse.deadlineTime())))
                .andExpect(jsonPath("$.data.missionInfo.price").value(missionInfoResponse.price()))
                .andExpect(jsonPath("$.data.bookmarkCount").value(missionResponse.bookmarkCount()))
                .andExpect(jsonPath("$.data.missionStatus").value(missionResponse.missionStatus()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }

    private MissionResponse createMissionResponse(
            MissionCategoryResponse missionCategoryResponse,
            MissionCreateRequest missionCreateRequest,
            MissionResponse.MissionInfoResponse missionInfoResponse
    ) {
        return MissionResponse.builder()
                .id(1L)
                .missionCategory(missionCategoryResponse)
                .citizenId(missionCreateRequest.citizenId())
                .regionId(missionCreateRequest.regionId())
                .location(new Point(missionCreateRequest.latitude(), missionCreateRequest.latitude()))
                .missionInfo(missionInfoResponse)
                .bookmarkCount(0)
                .missionStatus("MATCHING")
                .build();
    }

    private MissionResponse createMissionResponse(
            MissionCategoryResponse missionCategoryResponse,
            MissionUpdateRequest missionUpdateRequest,
            MissionResponse.MissionInfoResponse missionInfoResponse
    ) {
        return MissionResponse.builder()
                .id(1L)
                .missionCategory(missionCategoryResponse)
                .citizenId(missionUpdateRequest.citizenId())
                .regionId(missionUpdateRequest.regionId())
                .location(new Point(missionUpdateRequest.longitude(), missionUpdateRequest.latitude()))
                .missionInfo(missionInfoResponse)
                .bookmarkCount(0)
                .missionStatus("MATCHING")
                .build();
    }

    private MissionCategoryResponse createMissionCategoryResponse() {
        return MissionCategoryResponse.builder()
                .categoryId(1L)
                .code("MC_001")
                .name("서빙")
                .build();
    }

    private MissionResponse.MissionInfoResponse createMissionInfoResponse(
            MissionInfoRequest missionInfoRequest
    ) {
        return MissionResponse.MissionInfoResponse.builder()
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

    private MissionInfoRequest createMissionInfoRequest(
            LocalDate missionDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalTime deadlineTime
    ) {
        return MissionInfoRequest
                .builder()
                .content("내용")
                .missionDate(missionDate)
                .startTime(startTime)
                .endTime(endTime)
                .deadlineTime(deadlineTime)
                .price(10000)
                .build();
    }
}
