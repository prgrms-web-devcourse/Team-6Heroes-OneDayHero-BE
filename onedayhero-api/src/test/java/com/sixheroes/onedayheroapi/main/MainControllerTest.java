package com.sixheroes.onedayheroapi.main;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.main.request.UserPositionRequest;
import com.sixheroes.onedayheroapplication.main.MainService;
import com.sixheroes.onedayheroapplication.main.request.UserPositionServiceRequest;
import com.sixheroes.onedayheroapplication.main.response.MainResponse;
import com.sixheroes.onedayheroapplication.main.response.MissionSoonExpiredResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static com.sixheroes.onedayherocommon.converter.DateTimeConverter.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class MainControllerTest extends RestDocsSupport {

    @MockBean
    private MainService mainService;

    @Override
    protected Object setController() {
        return new MainController(mainService);
    }

    @DisplayName("메인 페이지를 조회 할 수 있다.")
    @Test
    void callMainPage() throws Exception {
        // given
        var userPosition = UserPositionRequest.builder()
                .longitude(127.02880308004335)
                .latitude(37.49779692073204)
                .build();

        var missionSoonExpiredResponseA = MissionSoonExpiredResponse.builder()
                .id(1L)
                .title("미션 제목1")
                .region(RegionResponse.builder()
                        .id(1L)
                        .si("서울시")
                        .gu("강남구")
                        .dong("역삼동")
                        .build())
                .missionCategory(
                        MissionCategoryResponse.builder()
                                .id(2L)
                                .code("MC_002")
                                .name("주방")
                                .build())
                .missionStatus("MATCHING")
                .missionDate(LocalDate.of(2023, 11, 22))
                .startTime(LocalTime.of(9, 0, 0))
                .endTime(LocalTime.of(13, 0, 0))
                .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 22),
                        LocalTime.of(9, 0, 0)
                ))
                .price(15000)
                .bookmarkCount(3)
                .imagePath("s3://path")
                .isBookmarked(false)
                .build();

        var missionSoonExpiredResponseB = MissionSoonExpiredResponse.builder()
                .id(2L)
                .title("미션 제목2")
                .region(RegionResponse.builder()
                        .id(2L)
                        .si("서울시")
                        .gu("강남구")
                        .dong("역삼동")
                        .build())
                .missionCategory(
                        MissionCategoryResponse.builder()
                                .id(3L)
                                .code("MC_003")
                                .name("배달, 운전")
                                .build())
                .missionStatus("MATCHING")
                .missionDate(LocalDate.of(2023, 11, 22))
                .startTime(LocalTime.of(9, 0, 0))
                .endTime(LocalTime.of(13, 0, 0))
                .deadlineTime(LocalDateTime.of(
                        LocalDate.of(2023, 11, 22),
                        LocalTime.of(9, 0, 0)
                ))
                .price(15000)
                .bookmarkCount(3)
                .imagePath("s3://path")
                .isBookmarked(false)
                .build();

        var missionCategories = List.of(MissionCategoryResponse.builder()
                        .id(1L)
                        .code("MC_001")
                        .name("서빙")
                        .build(),
                MissionCategoryResponse.builder()
                        .id(2L)
                        .code("MC_002")
                        .name("주방")
                        .build(),
                MissionCategoryResponse.builder()
                        .id(3L)
                        .code("MC_003")
                        .name("배달, 운전")
                        .build());


        var mainResponse = MainResponse.builder()
                .missionCategories(missionCategories)
                .soonExpiredMissions(
                        List.of(missionSoonExpiredResponseA, missionSoonExpiredResponseB)
                ).build();

        given(mainService.findMainResponse(any(Long.class), any(UserPositionServiceRequest.class), any(LocalDateTime.class)))
                .willReturn(mainResponse);

        // when & then
        mockMvc.perform(get("/api/v1/main")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("longitude", String.valueOf(userPosition.longitude()))
                        .param("latitude", String.valueOf(userPosition.latitude()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("main-page",
                        requestHeaders(
                                headerWithName("Authorization").description("Auth Credential")
                        ),
                        queryParameters(
                                parameterWithName("longitude").description("경도"),
                                parameterWithName("latitude").description("위도")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.missionCategories").type(JsonFieldType.ARRAY)
                                        .description("미션 카테고리 정보 배열"),
                                fieldWithPath("data.missionCategories[].id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategories[].code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategories[].name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.soonExpiredMissions").type(JsonFieldType.ARRAY)
                                        .description("곧 마감될 미션 배열"),
                                fieldWithPath("data.soonExpiredMissions[].id").type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("data.soonExpiredMissions[].title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.soonExpiredMissions[].region.id").type(JsonFieldType.NUMBER)
                                        .description("지역 ID"),
                                fieldWithPath("data.soonExpiredMissions[].region.si").type(JsonFieldType.STRING)
                                        .description("지역 시"),
                                fieldWithPath("data.soonExpiredMissions[].region.gu").type(JsonFieldType.STRING)
                                        .description("지역 구"),
                                fieldWithPath("data.soonExpiredMissions[].region.dong").type(JsonFieldType.STRING)
                                        .description("지역 동"),
                                fieldWithPath("data.soonExpiredMissions[].missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 객체"),
                                fieldWithPath("data.soonExpiredMissions[].missionCategory.id")
                                        .description("미션 카테고리 ID"),
                                fieldWithPath("data.soonExpiredMissions[].missionCategory.code")
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.soonExpiredMissions[].missionCategory.name")
                                        .description("미션 카테고리 이름"),
                                fieldWithPath("data.soonExpiredMissions[].missionDate").type(JsonFieldType.STRING)
                                        .attributes(getDateFormat())
                                        .description("미션 날짜"),
                                fieldWithPath("data.soonExpiredMissions[].startTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 시작 시간"),
                                fieldWithPath("data.soonExpiredMissions[].endTime").type(JsonFieldType.STRING)
                                        .attributes(getTimeFormat())
                                        .description("미션 종료 시간"),
                                fieldWithPath("data.soonExpiredMissions[].deadlineTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("미션 마감 날짜와 시간"),
                                fieldWithPath("data.soonExpiredMissions[].price").type(JsonFieldType.NUMBER)
                                        .description("미션 가격"),
                                fieldWithPath("data.soonExpiredMissions[].bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("북마크 횟수"),
                                fieldWithPath("data.soonExpiredMissions[].missionStatus").type(JsonFieldType.STRING)
                                        .description("미션 상태"),
                                fieldWithPath("data.soonExpiredMissions[].imagePath").type(JsonFieldType.STRING)
                                        .optional()
                                        .description("이미지 사진 경로"),
                                fieldWithPath("data.soonExpiredMissions[].isBookmarked").type(JsonFieldType.BOOLEAN)
                                        .description("북마크 상태"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )
                ))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.missionCategories[0].id").value(mainResponse.missionCategories().get(0).id()))
                .andExpect(jsonPath("$.data.missionCategories[0].code").value(mainResponse.missionCategories().get(0).code()))
                .andExpect(jsonPath("$.data.missionCategories[0].name").value(mainResponse.missionCategories().get(0).name()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].id").value(mainResponse.soonExpiredMissions().get(0).id()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].title").value(mainResponse.soonExpiredMissions().get(0).title()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].region.id").value(mainResponse.soonExpiredMissions().get(0).region().id()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].region.si").value(mainResponse.soonExpiredMissions().get(0).region().si()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].region.gu").value(mainResponse.soonExpiredMissions().get(0).region().gu()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].region.dong").value(mainResponse.soonExpiredMissions().get(0).region().dong()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].missionCategory.id").value(mainResponse.soonExpiredMissions().get(0).missionCategory().id()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].missionCategory.code").value(mainResponse.soonExpiredMissions().get(0).missionCategory().code()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].missionCategory.name").value(mainResponse.soonExpiredMissions().get(0).missionCategory().name()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].missionDate").value(convertDateToString(mainResponse.soonExpiredMissions().get(0).missionDate())))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].startTime").value(convertTimetoString(mainResponse.soonExpiredMissions().get(0).startTime())))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].endTime").value(convertTimetoString(mainResponse.soonExpiredMissions().get(0).endTime())))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].deadlineTime").value(convertLocalDateTimeToString(mainResponse.soonExpiredMissions().get(0).deadlineTime())))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].price").value(mainResponse.soonExpiredMissions().get(0).price()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].missionStatus").value(mainResponse.soonExpiredMissions().get(0).missionStatus()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].imagePath").value(mainResponse.soonExpiredMissions().get(0).imagePath()))
                .andExpect(jsonPath("$.data.soonExpiredMissions[0].isBookmarked").value(mainResponse.soonExpiredMissions().get(0).isBookmarked()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }
}
