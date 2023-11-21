package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.user.request.UserBasicInfoRequest;
import com.sixheroes.onedayheroapi.user.request.UserFavoriteWorkingDayRequest;
import com.sixheroes.onedayheroapi.user.request.UserUpdateRequest;
import com.sixheroes.onedayheroapplication.mission.MissionBookmarkService;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeResponse;
import com.sixheroes.onedayheroapplication.mission.response.MissionBookmarkMeViewResponse;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.SentReviewResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
import com.sixheroes.onedayheroapplication.user.request.UserServiceUpdateRequest;
import com.sixheroes.onedayheroapplication.user.response.*;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static com.sixheroes.onedayherocommon.converter.DateTimeConverter.convertDateToString;
import static com.sixheroes.onedayherocommon.converter.DateTimeConverter.convertTimetoString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest extends RestDocsSupport {

    @MockBean
    private UserService userService;

    @MockBean
    private MissionBookmarkService missionBookmarkService;

    @MockBean
    private ReviewService reviewService;

    @Override
    protected Object setController() {
        return new UserController(userService, missionBookmarkService, reviewService);
    }

    @DisplayName("유저 정보를 조회할 수 있다.")
    @Test
    void findUser() throws Exception {
        // given
        var userResponse = createUserResponse();

        given(userService.findUser(anyLong())).willReturn(userResponse);

        // when & then
        mockMvc.perform(get("/api/v1/me")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.basicInfo").exists())
            .andExpect(jsonPath("$.data.basicInfo.nickname").value(userResponse.basicInfo().nickname()))
            .andExpect(jsonPath("$.data.basicInfo.gender").value(userResponse.basicInfo().gender()))
            .andExpect(jsonPath("$.data.basicInfo.birth").value(DateTimeConverter.convertDateToString(userResponse.basicInfo().birth())))
            .andExpect(jsonPath("$.data.basicInfo.introduce").value(userResponse.basicInfo().introduce()))
            .andExpect(jsonPath("$.data.image").exists())
            .andExpect(jsonPath("$.data.image.originalName").value(userResponse.image().originalName()))
            .andExpect(jsonPath("$.data.image.uniqueName").value(userResponse.image().uniqueName()))
            .andExpect(jsonPath("$.data.image.path").value(userResponse.image().path()))
            .andExpect(jsonPath("$.data.favoriteWorkingDay").exists())
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteDate").isArray())
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteDate.[0]").value(userResponse.favoriteWorkingDay().favoriteDate().get(0)))
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteStartTime").value(DateTimeConverter.convertTimetoString(userResponse.favoriteWorkingDay().favoriteStartTime())))
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteEndTime").value(DateTimeConverter.convertTimetoString(userResponse.favoriteWorkingDay().favoriteEndTime())))
            .andExpect(jsonPath("$.data.favoriteRegions").exists())
            .andExpect(jsonPath("$.data.favoriteRegions[0].id").value(userResponse.favoriteRegions().get(0).id()))
            .andExpect(jsonPath("$.data.favoriteRegions[0].si").value(userResponse.favoriteRegions().get(0).si()))
            .andExpect(jsonPath("$.data.favoriteRegions[0].gu").value(userResponse.favoriteRegions().get(0).gu()))
            .andExpect(jsonPath("$.data.favoriteRegions[0].dong").value(userResponse.favoriteRegions().get(0).dong()))
            .andExpect(jsonPath("$.data.heroScore").value(userResponse.heroScore()))
            .andExpect(jsonPath("$.data.isHeroMode").value(userResponse.isHeroMode()))
            .andDo(document("user-find",
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.basicInfo").type(JsonFieldType.OBJECT).description("유저 기본 정보"),
                    fieldWithPath("data.basicInfo.nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("data.basicInfo.gender").type(JsonFieldType.STRING).description("성별"),
                    fieldWithPath("data.basicInfo.birth").type(JsonFieldType.STRING)
                        .attributes(getDateFormat())
                        .description("태어난 날짜"),
                    fieldWithPath("data.basicInfo.introduce")
                        .optional()
                        .description("자기 소개"),
                    fieldWithPath("data.image").type(JsonFieldType.OBJECT).description("프로필 이미지"),
                    fieldWithPath("data.image.originalName").type(JsonFieldType.STRING).description("원본 이름"),
                    fieldWithPath("data.image.uniqueName").type(JsonFieldType.STRING).description("고유 이름"),
                    fieldWithPath("data.image.path").type(JsonFieldType.STRING).description("이미지 경로"),
                    fieldWithPath("data.favoriteWorkingDay").type(JsonFieldType.OBJECT).description("희망 근무 정보"),
                    fieldWithPath("data.favoriteWorkingDay.favoriteDate")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description("희망 근무 요일"),
                    fieldWithPath("data.favoriteWorkingDay.favoriteStartTime")
                        .optional()
                        .attributes(getTimeFormat())
                        .type(JsonFieldType.STRING)
                        .description("희망 근무 시작 시간"),
                    fieldWithPath("data.favoriteWorkingDay.favoriteEndTime")
                        .optional()
                        .attributes(getTimeFormat())
                        .type(JsonFieldType.STRING)
                        .description("희망 근무 종료 시간"),
                    fieldWithPath("data.favoriteRegions")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description("선호 지역"),
                    fieldWithPath("data.favoriteRegions[].id")
                        .optional()
                        .type(JsonFieldType.NUMBER)
                        .description("지역 아이디"),
                    fieldWithPath("data.favoriteRegions[].si")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description("시 이름"),
                    fieldWithPath("data.favoriteRegions[].gu")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description("구 이름"),
                    fieldWithPath("data.favoriteRegions[].dong")
                        .optional()
                        .type(JsonFieldType.STRING)
                        .description("동 이름"),
                    fieldWithPath("data.heroScore").type(JsonFieldType.NUMBER).description("히어로 점수"),
                    fieldWithPath("data.isHeroMode").type(JsonFieldType.BOOLEAN).description("히어로 모드 활성 여부")
                )
            ));
    }

    @DisplayName("유저 정보를 수정할 수 있다.")
    @Test
    void updateUser() throws Exception {
        // given
        var userId = 1L;
        var userBasicInfoRequest = new UserBasicInfoRequest("이름", "MALE", LocalDate.of(1990, 1, 1), "자기 소개");
        var userFavoriteWorkingDayRequest = new UserFavoriteWorkingDayRequest(List.of("MON", "THU"), LocalTime.of(12, 0, 0), LocalTime.of(18, 0, 0));
        var userFavoriteRegions = List.of(1L, 2L);
        var userUpadateRequest = new UserUpdateRequest(userBasicInfoRequest, userFavoriteWorkingDayRequest, userFavoriteRegions);
        var userUpdateRequestToMultipartFile = createUserUpdateRequestToMultipartFile(objectMapper.writeValueAsString(userUpadateRequest));

        var userImage = createUserImage();

        var userUpdateResponse = new UserUpdateResponse(userId);

        given(userService.updateUser(anyLong(), any(UserServiceUpdateRequest.class), anyList())).willReturn(userUpdateResponse);

        // when & then
        mockMvc.perform(
            MockMvcRequestBuilders.multipart(HttpMethod.POST, "/api/v1/me")
                .file(userUpdateRequestToMultipartFile)
                .file(userImage)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(userId))
            .andDo(document("user-update",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                requestPartFields("userUpdateRequest",
                    fieldWithPath("basicInfo").type(JsonFieldType.OBJECT).description("유저 기본 정보"),
                    fieldWithPath("basicInfo.nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("basicInfo.gender").type(JsonFieldType.STRING).description("성별"),
                    fieldWithPath("basicInfo.birth").type(JsonFieldType.STRING)
                        .attributes(getDateFormat())
                        .description("태어난 날짜"),
                    fieldWithPath("basicInfo.introduce")
                        .optional()
                        .description("자기 소개"),
                    fieldWithPath("favoriteWorkingDay").type(JsonFieldType.OBJECT).description("희망 근무 정보"),
                    fieldWithPath("favoriteWorkingDay.favoriteDate")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description("희망 근무 요일"),
                    fieldWithPath("favoriteWorkingDay.favoriteStartTime")
                        .optional()
                        .attributes(getTimeFormat())
                        .type(JsonFieldType.STRING)
                        .description("희망 근무 시작 시간"),
                    fieldWithPath("favoriteWorkingDay.favoriteEndTime")
                        .optional()
                        .attributes(getTimeFormat())
                        .type(JsonFieldType.STRING)
                        .description("희망 근무 종료 시간"),
                    fieldWithPath("favoriteRegions")
                        .optional()
                        .type(JsonFieldType.ARRAY)
                        .description("선호 지역")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 아이디")
                )
            ));
    }

    @DisplayName("유저는 프로필 이미지를 삭제할 수 있다.")
    @Test
    void deleteUserImage() throws Exception {
        // given
        var userImageId = 1L;

        doNothing().when(userService).deleteUserImage(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/api/v1/me/user-images/{userImageId}", userImageId)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.status").value(204))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(document("user-image-delete",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                pathParameters(
                    parameterWithName("userImageId").description("유저 이미지 아이디")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()),
                    fieldWithPath("data").type(JsonFieldType.NULL)
                        .description("응답 데이터")
                )
            ));
    }

    @DisplayName("유저는 내가 찜한 미션 목록을 조회할 수 있다.")
    @Test
    void viewMeBookmarkMissions() throws Exception {
        // when
        var userId = 1L;
        var missionBookmarkMeDtoA = createMissionBookmarkMeDtoA();
        var missionBookmarkMeDtoB = createMissionBookmarkMeDtoB();
        var missionBookmarkMeDtoC = createMissionBookmarkMeDtoC();

        var content = List.of(
                missionBookmarkMeDtoA,
                missionBookmarkMeDtoB,
                missionBookmarkMeDtoC
        );

        var pageRequest = PageRequest.of(
                0,
                3
        );
        var response = new SliceImpl<MissionBookmarkMeResponse>(
                content,
                pageRequest,
                true
        );

        given(missionBookmarkService.viewMyBookmarks(
                        any(Pageable.class),
                        anyLong()
                )
        ).willReturn(
                new MissionBookmarkMeViewResponse(
                        userId,
                        response
                )
        );

        // when & then
        mockMvc.perform(get("/api/v1/me/bookmarks")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("page", "0")
                        .param("size", "3")
                        .param("sort", "")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionId").value(missionBookmarkMeDtoA.missionId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionBookmarkId").value(missionBookmarkMeDtoA.missionBookmarkId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].isAlive").value(missionBookmarkMeDtoA.isAlive()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.title").value(missionBookmarkMeDtoA.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.bookmarkCount").value(missionBookmarkMeDtoA.missionInfo().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.price").value(missionBookmarkMeDtoA.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.missionDate").value(convertDateToString(missionBookmarkMeDtoA.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.startTime").value(convertTimetoString(missionBookmarkMeDtoA.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.endTime").value(convertTimetoString(missionBookmarkMeDtoA.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].missionInfo.categoryName").value(missionBookmarkMeDtoA.missionInfo().categoryName()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].region.si").value(missionBookmarkMeDtoA.region().si()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].region.gu").value(missionBookmarkMeDtoA.region().gu()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[0].region.dong").value(missionBookmarkMeDtoA.region().dong()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionId").value(missionBookmarkMeDtoB.missionId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionBookmarkId").value(missionBookmarkMeDtoB.missionBookmarkId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].isAlive").value(missionBookmarkMeDtoB.isAlive()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.title").value(missionBookmarkMeDtoB.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.bookmarkCount").value(missionBookmarkMeDtoB.missionInfo().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.price").value(missionBookmarkMeDtoB.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.missionDate").value(convertDateToString(missionBookmarkMeDtoB.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.startTime").value(convertTimetoString(missionBookmarkMeDtoB.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.endTime").value(convertTimetoString(missionBookmarkMeDtoB.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].missionInfo.categoryName").value(missionBookmarkMeDtoB.missionInfo().categoryName()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].region.si").value(missionBookmarkMeDtoB.region().si()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].region.gu").value(missionBookmarkMeDtoB.region().gu()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[1].region.dong").value(missionBookmarkMeDtoB.region().dong()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionId").value(missionBookmarkMeDtoC.missionId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionBookmarkId").value(missionBookmarkMeDtoC.missionBookmarkId()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].isAlive").value(missionBookmarkMeDtoC.isAlive()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.title").value(missionBookmarkMeDtoC.missionInfo().title()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.bookmarkCount").value(missionBookmarkMeDtoC.missionInfo().bookmarkCount()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.price").value(missionBookmarkMeDtoC.missionInfo().price()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.missionDate").value(convertDateToString(missionBookmarkMeDtoC.missionInfo().missionDate())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.startTime").value(convertTimetoString(missionBookmarkMeDtoC.missionInfo().startTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.endTime").value(convertTimetoString(missionBookmarkMeDtoC.missionInfo().endTime())))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].missionInfo.categoryName").value(missionBookmarkMeDtoC.missionInfo().categoryName()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].region.si").value(missionBookmarkMeDtoC.region().si()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].region.gu").value(missionBookmarkMeDtoC.region().gu()))
                .andExpect(jsonPath("$.data.missionBookmarkMeResponses.content[2].region.dong").value(missionBookmarkMeDtoC.region().dong()))
                .andExpect(jsonPath("$.serverDateTime").exists())
                .andDo(document("user-bookmark",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
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
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("내 유저 아이디"),
                                fieldWithPath("data.missionBookmarkMeResponses").type(JsonFieldType.OBJECT)
                                        .description("내 찜 미션 목록"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[]").type(JsonFieldType.ARRAY)
                                        .description("찜 미션 목록 배열"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionId").type(JsonFieldType.NUMBER)
                                        .description("찜한 미션 아이디"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionBookmarkId").type(JsonFieldType.NUMBER)
                                        .description("북마크 아이디"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].isAlive").type(JsonFieldType.BOOLEAN)
                                        .description("아직 매칭 가능한 미션일 경우 true"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.title").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.categoryName").type(JsonFieldType.STRING)
                                        .description("미션 카테고리"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.bookmarkCount").type(JsonFieldType.NUMBER)
                                        .description("미션 북마크 수"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.price").type(JsonFieldType.NUMBER)
                                        .description("미션 수익"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.missionDate").type(JsonFieldType.STRING)
                                        .description("미션 수행 일")
                                        .attributes(getDateFormat()),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.startTime").type(JsonFieldType.STRING)
                                        .description("미션 시작 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].missionInfo.endTime").type(JsonFieldType.STRING)
                                        .description("미션 종료 시간")
                                        .attributes(getTimeFormat()),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].region.id").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].region.si").type(JsonFieldType.STRING)
                                        .description("시"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].region.gu").type(JsonFieldType.STRING)
                                        .description("구"),
                                fieldWithPath("data.missionBookmarkMeResponses.content[].region.dong").type(JsonFieldType.STRING)
                                        .description("동"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.size").type(JsonFieldType.NUMBER)
                                        .description("미션 리스트 크기"),
                                fieldWithPath("data.missionBookmarkMeResponses.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.missionBookmarkMeResponses.sort").type(JsonFieldType.OBJECT)
                                        .description("미션 리스트 정렬 정보 객체"),
                                fieldWithPath("data.missionBookmarkMeResponses.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 있는지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.missionBookmarkMeResponses.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.missionBookmarkMeResponses.empty").type(JsonFieldType.BOOLEAN)
                                        .description("미션 리스트가 비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )));
    }

    @DisplayName("히어로 모드를 활성화할 수 있다.")
    @Test
    void turnOnHeroMode() throws Exception {
        // given
        var userId = 1L;

        var userUpdateResponse = new UserUpdateResponse(userId);
        given(userService.turnOnHeroMode(anyLong())).willReturn(userUpdateResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/me/change-hero")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(userId))
            .andDo(document("user-change-hero",
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                        .description("유저 아이디")
                )
            ));
    }

    @DisplayName("히어로 모드를 비활성화할 수 있다.")
    @Test
    void turnOffHeroMode() throws Exception {
        // given
        var userId = 1L;

        var userUpdateResponse = new UserUpdateResponse(userId);
        given(userService.turnOffHeroMode(anyLong())).willReturn(userUpdateResponse);

        // when & then
        mockMvc.perform(patch("/api/v1/me/change-citizen")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(userId))
            .andDo(document("user-change-citizen",
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .description("서버 응답 시간")
                        .attributes(getDateTimeFormat()),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("유저 아이디")
                )
            ));
    }

    @DisplayName("내가 쓴 리뷰를 확인할 수 있다.")
    @Test
    void viewSentReviews() throws Exception {
        // given
        var sentReviewA = createSentReviewA();
        var sentReviewB = createSentReviewB();
        var sentReviewResponses = new SliceImpl<SentReviewResponse>(
                List.of(sentReviewA, sentReviewB),
                PageRequest.of(0, 5),
                true
        );

        given(reviewService.viewSentReviews(any(Pageable.class), anyLong())).willReturn(sentReviewResponses);

        // when & then
        mockMvc.perform(get("/api/v1/me/reviews/send")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].reviewId").value(sentReviewA.reviewId()))
                .andExpect(jsonPath("$.data.content[0].categoryName").value(sentReviewA.categoryName()))
                .andExpect(jsonPath("$.data.content[0].missionTitle").value(sentReviewA.missionTitle()))
                .andExpect(jsonPath("$.data.content[0].starScore").value(sentReviewA.starScore()))
                .andExpect(jsonPath("$.data.content[0].senderNickname").value(sentReviewA.senderNickname()))
                .andExpect(jsonPath("$.data.content[0].profileImage").isArray())
                .andExpect(jsonPath("$.data.content[0].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(sentReviewA.createdAt())))
                .andExpect(jsonPath("$.data.content[1].reviewId").value(sentReviewB.reviewId()))
                .andExpect(jsonPath("$.data.content[1].categoryName").value(sentReviewB.categoryName()))
                .andExpect(jsonPath("$.data.content[1].missionTitle").value(sentReviewB.missionTitle()))
                .andExpect(jsonPath("$.data.content[1].starScore").value(sentReviewB.starScore()))
                .andExpect(jsonPath("$.data.content[1].senderNickname").value(sentReviewB.senderNickname()))
                .andExpect(jsonPath("$.data.content[1].profileImage").isArray())
                .andExpect(jsonPath("$.data.content[1].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(sentReviewB.createdAt())))
                    .andDo(document("user-sent-reviews",
                            requestHeaders(
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
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
                                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                            .description("내가 작성한 리뷰 목록 배열"),
                                    fieldWithPath("data.content[].reviewId").type(JsonFieldType.NUMBER)
                                            .description("내가 작성한 리뷰 아이디"),
                                    fieldWithPath("data.content[].categoryName").type(JsonFieldType.STRING)
                                            .description("내가 리뷰를 작성한 미션의 카테고리 이름"),
                                    fieldWithPath("data.content[].missionTitle").type(JsonFieldType.STRING)
                                            .description("내가 리뷰를 작성한 미션의 제목"),
                                    fieldWithPath("data.content[].starScore").type(JsonFieldType.NUMBER)
                                            .description("내가 준 별점"),
                                    fieldWithPath("data.content[].senderNickname").type(JsonFieldType.STRING)
                                            .description("내 닉네임"),
                                    fieldWithPath("data.content[].profileImage").type(JsonFieldType.ARRAY)
                                            .description("내 프로필 이미지 경로")
                                            .optional(),
                                    fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING)
                                            .description("내가 리뷰를 작성한 시간"),
                                    fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                                    .description("현재 페이지 번호"),
                                    fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                            .description("페이지 크기"),
                                    fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT)
                                            .description("정렬 상태 객체"),
                                    fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                            .description("정렬 정보가 비어있는지 여부"),
                                    fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                            .description("정렬 정보가 있는지 여부"),
                                    fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                            .description("정렬 정보가 정렬되지 않은지 여부"),
                                    fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                            .description("페이지 번호"),
                                    fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                            .description("페이징이 되어 있는지 여부"),
                                    fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                            .description("페이징이 되어 있지 않은지 여부"),
                                    fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                            .description("현재 페이지 조회에서 가져온 리뷰 개수"),
                                    fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                            .description("현재 페이지 번호"),
                                    fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                                            .description("정렬 정보 객체"),
                                    fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                            .description("정렬 정보가 비어있는지 여부"),
                                    fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                            .description("정렬 정보가 있는지 여부"),
                                    fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                            .description("정렬 정보가 정렬되지 않은지 여부"),
                                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                            .description("현재 페이지의 요소 수"),
                                    fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                            .description("첫 번째 페이지인지 여부"),
                                    fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                            .description("마지막 페이지인지 여부"),
                                    fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                            .description("비어있는지 여부"),
                                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                            .attributes(getDateTimeFormat())
                                            .description("서버 응답 시간")
                            )));
    }

    @DisplayName("내가 받은 리뷰를 확인할 수 있다.")
    @Test
    void viewReceivedReviews() throws Exception {
        // given
        var receivedReviewA = createReceivedReviewA();
        var receivedReviewB = createReceivedReviewB();

        var receivedReviewResponses = new SliceImpl<ReceivedReviewResponse>(
                List.of(receivedReviewA, receivedReviewB),
                PageRequest.of(0, 5),
                true
        );

        given(reviewService.viewReceivedReviews(any(Pageable.class), anyLong())).willReturn(receivedReviewResponses);

        mockMvc.perform(get("/api/v1/me/reviews/receive")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].reviewId").value(receivedReviewA.reviewId()))
                .andExpect(jsonPath("$.data.content[0].senderId").value(receivedReviewA.senderId()))
                .andExpect(jsonPath("$.data.content[0].senderNickname").value(receivedReviewA.senderNickname()))
                .andExpect(jsonPath("$.data.content[0].profileImage").isArray())
                .andExpect(jsonPath("$.data.content[0].categoryName").value(receivedReviewA.categoryName()))
                .andExpect(jsonPath("$.data.content[0].missionTitle").value(receivedReviewA.missionTitle()))
                .andExpect(jsonPath("$.data.content[0].starScore").value(receivedReviewA.starScore()))
                .andExpect(jsonPath("$.data.content[0].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(receivedReviewA.createdAt())))
                .andExpect(jsonPath("$.data.content[1].reviewId").value(receivedReviewB.reviewId()))
                .andExpect(jsonPath("$.data.content[1].senderId").value(receivedReviewB.senderId()))
                .andExpect(jsonPath("$.data.content[1].senderNickname").value(receivedReviewB.senderNickname()))
                .andExpect(jsonPath("$.data.content[1].profileImage").isArray())
                .andExpect(jsonPath("$.data.content[1].categoryName").value(receivedReviewB.categoryName()))
                .andExpect(jsonPath("$.data.content[1].missionTitle").value(receivedReviewB.missionTitle()))
                .andExpect(jsonPath("$.data.content[1].starScore").value(receivedReviewB.starScore()))
                .andExpect(jsonPath("$.data.content[1].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(receivedReviewB.createdAt())))
                .andDo(document("user-received-reviews",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
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
                                fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                                        .description("내가 받은 리뷰 목록 배열"),
                                fieldWithPath("data.content[].reviewId").type(JsonFieldType.NUMBER)
                                        .description("내가 받은 리뷰의 아이디"),
                                fieldWithPath("data.content[].senderId").type(JsonFieldType.NUMBER)
                                        .description("내가 받은 리뷰 작성 유저 아이디"),
                                fieldWithPath("data.content[].senderNickname").type(JsonFieldType.STRING)
                                        .description("내가 받은 리뷰 작성 유저 닉네임"),
                                fieldWithPath("data.content[].profileImage").type(JsonFieldType.ARRAY)
                                        .description("내가 받은 리뷰를 작성한 유저의 프로필 이미지 경로")
                                        .optional(),
                                fieldWithPath("data.content[].categoryName").type(JsonFieldType.STRING)
                                        .description("내가 받은 리뷰의 미션 카테고리 이름"),
                                fieldWithPath("data.content[].missionTitle").type(JsonFieldType.STRING)
                                        .description("내가 받은 리뷰의 미션 제목"),
                                fieldWithPath("data.content[].starScore").type(JsonFieldType.NUMBER)
                                        .description("내가 받은 별점"),
                                fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING)
                                        .description("내가 리뷰를 받은 시간"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 조회에서 가져온 리뷰 개수"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 정보 객체"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                        .description("비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )));
    }

    private MissionBookmarkMeResponse createMissionBookmarkMeDtoA() {
        return MissionBookmarkMeResponse.builder()
                .missionId(1L)
                .missionBookmarkId(5L)
                .isAlive(true)
                .missionInfo(MissionBookmarkMeResponse.MissionBookmarkMeMissionInfoDto.builder()
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

    private MissionBookmarkMeResponse createMissionBookmarkMeDtoB() {
        return MissionBookmarkMeResponse.builder()
                .missionId(2L)
                .missionBookmarkId(6L)
                .isAlive(true)
                .missionInfo(MissionBookmarkMeResponse.MissionBookmarkMeMissionInfoDto.builder()
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
    private MissionBookmarkMeResponse createMissionBookmarkMeDtoC() {
        return MissionBookmarkMeResponse.builder()
                .missionId(5L)
                .missionBookmarkId(10L)
                .isAlive(true)
                .missionInfo(MissionBookmarkMeResponse.MissionBookmarkMeMissionInfoDto.builder()
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

    private SentReviewResponse createSentReviewA() {
        return SentReviewResponse.builder()
                .reviewId(1L)
                .categoryName("심부름")
                .missionTitle("심부름 미션")
                .starScore(3)
                .senderNickname("리뷰 작성자 닉네임")
                .profileImage(List.of("s3 프로필 이미지 주소"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private SentReviewResponse createSentReviewB() {
        return SentReviewResponse.builder()
                .reviewId(2L)
                .categoryName("청소")
                .missionTitle("청소 미션")
                .starScore(4)
                .senderNickname("리뷰 작성자 닉네임")
                .profileImage(List.of("s3 프로필 이미지 주소"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ReceivedReviewResponse createReceivedReviewA() {
        return ReceivedReviewResponse.builder()
                .reviewId(1L)
                .senderId(5L)
                .senderNickname("nickname A")
                .profileImage(List.of("s3 프로필 이미지 주소"))
                .categoryName("청소")
                .missionTitle("청소 미션")
                .starScore(4)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ReceivedReviewResponse createReceivedReviewB() {
        return ReceivedReviewResponse.builder()
                .reviewId(2L)
                .senderId(8L)
                .senderNickname("nickname B")
                .profileImage(List.of("s3 프로필 이미지 주소"))
                .categoryName("심부름")
                .missionTitle("심부름 미션")
                .starScore(3)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserResponse createUserResponse() {
        return new UserResponse(
            createUserBasicInfoResponse(),
            createUserImageResponse(),
            createUserFavoriteWorkingDayResponse(),
            createRegionResponses(),
            30,
            false
        );
    }

    private List<RegionResponse> createRegionResponses() {
        var regionResponse1 = RegionResponse.builder()
            .id(1L)
            .si("서울시")
            .gu("강남구")
            .dong("역삼동")
            .build();

        var regionResponse2 = RegionResponse.builder()
            .id(2L)
            .si("서울시")
            .gu("강남구")
            .dong("청담동")
            .build();

        return List.of(regionResponse1, regionResponse2);
    }

    private UserBasicInfoResponse createUserBasicInfoResponse() {
        return new UserBasicInfoResponse("이름", "MALE", LocalDate.of(1990, 1, 1), "자기 소개");
    }

    private UserImageResponse createUserImageResponse() {
        return new UserImageResponse("profile.jpg", "unique.jpg", "http://");
    }

    private UserFavoriteWorkingDayResponse createUserFavoriteWorkingDayResponse() {
        return new UserFavoriteWorkingDayResponse(List.of("MON", "THU"), LocalTime.of(12, 0, 0), LocalTime.of(18, 0, 0));
    }

    private MockMultipartFile createUserUpdateRequestToMultipartFile(
        String json
    ) {
        return new MockMultipartFile(
            "userUpdateRequest",
            "json",
            MediaType.APPLICATION_JSON.toString(),
            json.getBytes(StandardCharsets.UTF_8)
        );
    }

    private MockMultipartFile createUserImage() {
        return new MockMultipartFile(
            "images",
            "imageA.jpeg",
            "image/jpeg",
            "<<jpeg data>>".getBytes()
        );
    }
}
