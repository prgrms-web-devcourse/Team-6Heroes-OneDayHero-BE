package com.sixheroes.onedayheroapi.mission;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCancelServiceRequest;
import com.sixheroes.onedayheroapplication.mission.request.MissionBookmarkCreateServiceRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCancelResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkCreateResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeLineDto;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static com.sixheroes.onedayherocommon.converter.DateTimeConverter.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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

    @DisplayName("유저는 미션 찜목록을 조회할 수 있다.")
    @Test
    void viewMeBookmarkMissions() throws Exception {
        // when
        var missionBookmarkMeDtoA = createMissionBookmarkMeDtoA();
        var missionBookmarkMeDtoB = createMissionBookmarkMeDtoB();
        var missionBookmarkMeDtoC = createMissionBookmarkMeDtoC();

        var content = List.of(
                missionBookmarkMeDtoA,
                missionBookmarkMeDtoB,
                missionBookmarkMeDtoC
        );

        var pageRequest = PageRequest.of(0, 3);
        var lineDtos = new SliceImpl<MissionBookmarkMeLineDto>(
                content,
                pageRequest,
                true
        );

        given(missionBookmarkService.me(
                any(Pageable.class),
                anyLong()
                )
        ).willReturn(new MissionBookmarkMeViewResponse(lineDtos));


        // when & then
        mockMvc.perform(get("/api/v1/bookmarks/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer: accessToken")
                .param("page", "0")
                .param("size", "3")
                .param("sort", "")
                .param("userId", "1")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionId").value(missionBookmarkMeDtoA.missionId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionBookmarkId").value(missionBookmarkMeDtoA.missionBookmarkId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].isAlive").value(missionBookmarkMeDtoA.isAlive()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.title").value(missionBookmarkMeDtoA.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.bookmarkCount").value(missionBookmarkMeDtoA.missionInfo().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.price").value(missionBookmarkMeDtoA.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.missionDate").value(convertDateToString(missionBookmarkMeDtoA.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.startTime").value(convertTimetoString(missionBookmarkMeDtoA.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.endTime").value(convertTimetoString(missionBookmarkMeDtoA.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].missionInfo.categoryName").value(missionBookmarkMeDtoA.missionInfo().categoryName()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].region.si").value(missionBookmarkMeDtoA.region().si()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].region.gu").value(missionBookmarkMeDtoA.region().gu()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[0].region.dong").value(missionBookmarkMeDtoA.region().dong()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionId").value(missionBookmarkMeDtoB.missionId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionBookmarkId").value(missionBookmarkMeDtoB.missionBookmarkId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].isAlive").value(missionBookmarkMeDtoB.isAlive()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.title").value(missionBookmarkMeDtoB.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.bookmarkCount").value(missionBookmarkMeDtoB.missionInfo().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.price").value(missionBookmarkMeDtoB.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.missionDate").value(convertDateToString(missionBookmarkMeDtoB.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.startTime").value(convertTimetoString(missionBookmarkMeDtoB.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.endTime").value(convertTimetoString(missionBookmarkMeDtoB.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].missionInfo.categoryName").value(missionBookmarkMeDtoB.missionInfo().categoryName()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].region.si").value(missionBookmarkMeDtoB.region().si()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].region.gu").value(missionBookmarkMeDtoB.region().gu()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[1].region.dong").value(missionBookmarkMeDtoB.region().dong()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionId").value(missionBookmarkMeDtoC.missionId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionBookmarkId").value(missionBookmarkMeDtoC.missionBookmarkId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].isAlive").value(missionBookmarkMeDtoC.isAlive()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.title").value(missionBookmarkMeDtoC.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.bookmarkCount").value(missionBookmarkMeDtoC.missionInfo().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.price").value(missionBookmarkMeDtoC.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.missionDate").value(convertDateToString(missionBookmarkMeDtoC.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.startTime").value(convertTimetoString(missionBookmarkMeDtoC.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.endTime").value(convertTimetoString(missionBookmarkMeDtoC.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].missionInfo.categoryName").value(missionBookmarkMeDtoC.missionInfo().categoryName()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].region.si").value(missionBookmarkMeDtoC.region().si()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].region.gu").value(missionBookmarkMeDtoC.region().gu()))
                .andExpect(jsonPath("$.data.missionBookmarkMeLineDtos.content[2].region.dong").value(missionBookmarkMeDtoC.region().dong()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("mission-bookmark-me",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                        ),
                        queryParameters(
                                parameterWithName("page").optional()
                                        .description("페이지 번호"),
                                parameterWithName("size").optional()
                                        .description("데이터 크기"),
                                parameterWithName("sort").optional()
                                        .description("정렬 기준 필드"),
                                parameterWithName("userId").optional()
                                        .description("유저 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.missionBookmarkMeLineDtos").type(JsonFieldType.OBJECT)
                                        .description("내 찜 미션 목록"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[]").type(JsonFieldType.ARRAY)
                                        .description("찜 미션 목록 배열"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionId").type(JsonFieldType.NUMBER)
                                        .description("찜한 미션 아이디"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionBookmarkId").type(JsonFieldType.NUMBER)
                                        .description("북마크 아이디"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].isAlive").type(JsonFieldType.BOOLEAN)
                                        .description("아직 매칭 가능한 미션일 경우 true"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.categoryName").type(JsonFieldType.STRING)
                                        .description("미션 카테고리"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 북마크 수"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 수익"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].region.id").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].region.si").type(JsonFieldType.STRING)
                                        .description("시"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].region.gu").type(JsonFieldType.STRING)
                                        .description("구"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.content[].region.dong").type(JsonFieldType.STRING)
                                        .description("동"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.size").type(JsonFieldType.NUMBER)
                                        .description("미션 리스트 크기"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.sort").type(JsonFieldType.OBJECT)
                                        .description("미션 리스트 정렬 정보 객체"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.missionBookmarkMeLineDtos.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트가 비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                                )));
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
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
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

    private MissionBookmarkMeLineDto createMissionBookmarkMeDtoA() {
        return MissionBookmarkMeLineDto.builder()
                .missionId(1L)
                .missionBookmarkId(5L)
                .isAlive(true)
                .missionInfo(MissionBookmarkMeLineDto.MissionBookmarkMeMissionInfoDto.builder()
                        .title("청소 미션")
                        .bookmarkCount(5)
                        .price(35000)
                        .missionDate(LocalDate.of(2023, 12, 5))
                        .startTime(LocalTime.of(14, 30))
                        .endTime(LocalTime.of(18, 30))
                        .categoryName("청소")
                        .build())
                .region(RegionResponse.builder()
                        .id(1L)
                        .si("서울시")
                        .gu("강남구")
                        .dong("역삼동")
                        .build())
                .build();
    }

    private MissionBookmarkMeLineDto createMissionBookmarkMeDtoB() {
        return MissionBookmarkMeLineDto.builder()
                .missionId(2L)
                .missionBookmarkId(6L)
                .isAlive(true)
                .missionInfo(MissionBookmarkMeLineDto.MissionBookmarkMeMissionInfoDto.builder()
                        .title("심부름1")
                        .categoryName("심부름")
                        .bookmarkCount(3)
                        .price(10000)
                        .missionDate(LocalDate.of(2023, 12, 6))
                        .startTime(LocalTime.of(10, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build())
                .region(RegionResponse.builder()
                        .id(1L)
                        .si("서울시")
                        .gu("강남구")
                        .dong("역삼동")
                        .build())
                .build();
    }
    private MissionBookmarkMeLineDto createMissionBookmarkMeDtoC() {
        return MissionBookmarkMeLineDto.builder()
                .missionId(5L)
                .missionBookmarkId(10L)
                .isAlive(true)
                .missionInfo(MissionBookmarkMeLineDto.MissionBookmarkMeMissionInfoDto.builder()
                        .title("심부름2")
                        .categoryName("심부름")
                        .bookmarkCount(3)
                        .price(10000)
                        .missionDate(LocalDate.of(2023, 12, 7))
                        .startTime(LocalTime.of(12, 30))
                        .endTime(LocalTime.of(13, 30))
                        .build())
                .region(RegionResponse.builder()
                        .id(1L)
                        .si("서울시")
                        .gu("강남구")
                        .dong("역삼동")
                        .build())
                .build();
    }
}
