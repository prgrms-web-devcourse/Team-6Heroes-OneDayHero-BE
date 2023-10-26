package com.sixheroes.onedayheroapi.mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixheroes.onedayheroapi.mission.request.MissionCreateRequest;
import com.sixheroes.onedayheroapi.mission.request.MissionInfoRequest;
import com.sixheroes.onedayheroapplication.mission.MissionService;
import com.sixheroes.onedayheroapplication.mission.request.MissionCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionResponse;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionController.class)
class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MissionService missionService;

    @DisplayName("시민은 미션을 작성 할 수 있다.")
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
                .andExpect(jsonPath("$.status").value(201))
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
                .andExpect(jsonPath("$.data.bookmarkCount").value(0))
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