package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewViewResponse;
import com.sixheroes.onedayheroapplication.user.UserService;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getTimeFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest extends RestDocsSupport {

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;


    @Override
    protected Object setController() {
        return new ProfileController(userService, reviewService);
    }

    @DisplayName("유저의 시민 프로필을 조회한다.")
    @Test
    void findCitizenProfile() throws Exception {
        // given
        var userId = 1L;

        var userBasicInfoResponse = new UserBasicInfoResponse("이름", "MALE", LocalDate.of(1990, 1, 1), null);
        var userImageResponse = new UserImageResponse("profile.jpg", "unique.jpg", "http://");
        var heroScore = 60;

        var profileCitizenResponse = new ProfileCitizenResponse(userBasicInfoResponse, userImageResponse, heroScore);

        given(userService.findCitizenProfile(anyLong())).willReturn(profileCitizenResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/{userId}/citizen", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.basicInfo").exists())
            .andExpect(jsonPath("$.data.basicInfo.nickname").value(userBasicInfoResponse.nickname()))
            .andExpect(jsonPath("$.data.basicInfo.gender").value(userBasicInfoResponse.gender()))
            .andExpect(jsonPath("$.data.basicInfo.birth").value(DateTimeConverter.convertDateToString(userBasicInfoResponse.birth())))
            .andExpect(jsonPath("$.data.basicInfo.introduce").doesNotExist())
            .andExpect(jsonPath("$.data.image").exists())
            .andExpect(jsonPath("$.data.image.originalName").value(userImageResponse.originalName()))
            .andExpect(jsonPath("$.data.image.uniqueName").value(userImageResponse.uniqueName()))
            .andExpect(jsonPath("$.data.image.path").value(userImageResponse.path()))
            .andExpect(jsonPath("$.data.heroScore").value(heroScore))
            .andDo(document("profile-citizen-find",
                pathParameters(
                    parameterWithName("userId").description("유저 아이디")
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
                    fieldWithPath("data.image").type(JsonFieldType.OBJECT).description("프로필 이미지"),
                    fieldWithPath("data.image.originalName").type(JsonFieldType.STRING).description("원본 이름"),
                    fieldWithPath("data.image.uniqueName").type(JsonFieldType.STRING).description("고유 이름"),
                    fieldWithPath("data.image.path").type(JsonFieldType.STRING).description("이미지 경로"),
                    fieldWithPath("data.heroScore").type(JsonFieldType.NUMBER).description("히어로 점수")
                )
            ));
    }

    @DisplayName("유저의 히어로 프로필을 조회한다.")
    @Test
    void findHeroProfile() throws Exception {
        // given
        var userId = 1L;

        var userBasicInfoResponse = new UserBasicInfoResponse("이름", "MALE", LocalDate.of(1990, 1, 1), "자기 소개");
        var userImageResponse = new UserImageResponse("profile.jpg", "unique.jpg", "http://");
        var userFavoriteWorkingDayResponse = new UserFavoriteWorkingDayResponse(List.of("MON", "THU"), LocalTime.of(12, 0, 0), LocalTime.of(18, 0, 0));
        var heroScore = 60;

        var profileHeroResponse = new ProfileHeroResponse(userBasicInfoResponse, userImageResponse, userFavoriteWorkingDayResponse, heroScore);

        given(userService.findHeroProfile(anyLong())).willReturn(profileHeroResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/{userId}/hero", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.basicInfo").exists())
            .andExpect(jsonPath("$.data.basicInfo.nickname").value(userBasicInfoResponse.nickname()))
            .andExpect(jsonPath("$.data.basicInfo.gender").value(userBasicInfoResponse.gender()))
            .andExpect(jsonPath("$.data.basicInfo.birth").value(DateTimeConverter.convertDateToString(userBasicInfoResponse.birth())))
            .andExpect(jsonPath("$.data.basicInfo.introduce").value(userBasicInfoResponse.introduce()))
            .andExpect(jsonPath("$.data.image").exists())
            .andExpect(jsonPath("$.data.image.originalName").value(userImageResponse.originalName()))
            .andExpect(jsonPath("$.data.image.uniqueName").value(userImageResponse.uniqueName()))
            .andExpect(jsonPath("$.data.image.path").value(userImageResponse.path()))
            .andExpect(jsonPath("$.data.favoriteWorkingDay").exists())
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteDate").isArray())
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteDate.[0]").value(userFavoriteWorkingDayResponse.favoriteDate().get(0)))
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteStartTime").value(DateTimeConverter.convertTimetoString(userFavoriteWorkingDayResponse.favoriteStartTime())))
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteEndTime").value(DateTimeConverter.convertTimetoString(userFavoriteWorkingDayResponse.favoriteEndTime())))
            .andExpect(jsonPath("$.data.heroScore").value(heroScore))
            .andDo(document("profile-hero-find",
                pathParameters(
                    parameterWithName("userId").description("유저 아이디")
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
                    fieldWithPath("data.heroScore").type(JsonFieldType.NUMBER).description("히어로 점수")
                )
            ));
    }

    @DisplayName("유저의 리뷰를 확인할 수 있다.")
    @Test
    void viewReceivedReviews() throws Exception {
        // given
        var userId = 1L;
        var receivedReviewA = createReceivedReviewA();
        var receivedReviewB = createReceivedReviewB();

        var receivedReviewResponses = new SliceImpl<ReceivedReviewResponse>(
                List.of(receivedReviewA, receivedReviewB),
                PageRequest.of(0, 5),
                true
        );

        var viewResponse = new ReceivedReviewViewResponse(userId, receivedReviewResponses);
        given(reviewService.viewReceivedReviews(any(Pageable.class), anyLong())).willReturn(viewResponse);

        mockMvc.perform(get("/api/v1/users/{userId}/receive-reviews", userId)
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[0].reviewId").value(receivedReviewA.reviewId()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[0].senderId").value(receivedReviewA.senderId()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[0].categoryName").value(receivedReviewA.categoryName()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[0].missionTitle").value(receivedReviewA.missionTitle()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[0].starScore").value(receivedReviewA.starScore()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[0].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(receivedReviewA.createdAt())))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[1].reviewId").value(receivedReviewB.reviewId()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[1].senderId").value(receivedReviewB.senderId()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[1].categoryName").value(receivedReviewB.categoryName()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[1].missionTitle").value(receivedReviewB.missionTitle()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[1].starScore").value(receivedReviewB.starScore()))
                .andExpect(jsonPath("$.data.receivedReviewResponses.content[1].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(receivedReviewB.createdAt())))
                .andDo(document("profile-received-reviews",
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
                                        .description("유저 아이디"),
                                fieldWithPath("data.receivedReviewResponses").type(JsonFieldType.OBJECT)
                                        .description("받은 리뷰 객체"),
                                fieldWithPath("data.receivedReviewResponses.content[]").type(JsonFieldType.ARRAY)
                                        .description("받은 리뷰의 목록 배열"),
                                fieldWithPath("data.receivedReviewResponses.content[].reviewId").type(JsonFieldType.NUMBER)
                                        .description("받은 리뷰의 아이디"),
                                fieldWithPath("data.receivedReviewResponses.content[].senderId").type(JsonFieldType.NUMBER)
                                        .description("받은 리뷰의 작성 유저 아이디"),
                                fieldWithPath("data.receivedReviewResponses.content[].categoryName").type(JsonFieldType.STRING)
                                        .description("받은 리뷰의 미션 카테고리 이름"),
                                fieldWithPath("data.receivedReviewResponses.content[].missionTitle").type(JsonFieldType.STRING)
                                        .description("받은 리뷰의 미션 제목"),
                                fieldWithPath("data.receivedReviewResponses.content[].starScore").type(JsonFieldType.NUMBER)
                                        .description("받은 별점"),
                                fieldWithPath("data.receivedReviewResponses.content[].createdAt").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("리뷰 작성 시간"),
                                fieldWithPath("data.receivedReviewResponses.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.receivedReviewResponses.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.receivedReviewResponses.pageable.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 상태 객체"),
                                fieldWithPath("data.receivedReviewResponses.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.receivedReviewResponses.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.receivedReviewResponses.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.receivedReviewResponses.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.receivedReviewResponses.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있는지 여부"),
                                fieldWithPath("data.receivedReviewResponses.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이징이 되어 있지 않은지 여부"),
                                fieldWithPath("data.receivedReviewResponses.size").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 조회에서 가져온 리뷰 개수"),
                                fieldWithPath("data.receivedReviewResponses.number").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지 번호"),
                                fieldWithPath("data.receivedReviewResponses.sort").type(JsonFieldType.OBJECT)
                                        .description("정렬 정보 객체"),
                                fieldWithPath("data.receivedReviewResponses.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 비어있는지 여부"),
                                fieldWithPath("data.receivedReviewResponses.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 있는지 여부"),
                                fieldWithPath("data.receivedReviewResponses.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬 정보가 정렬되지 않은지 여부"),
                                fieldWithPath("data.receivedReviewResponses.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.receivedReviewResponses.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 번째 페이지인지 여부"),
                                fieldWithPath("data.receivedReviewResponses.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지인지 여부"),
                                fieldWithPath("data.receivedReviewResponses.empty").type(JsonFieldType.BOOLEAN)
                                        .description("비어있는지 여부"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )));
    }

    private ReceivedReviewResponse createReceivedReviewA() {
        return ReceivedReviewResponse.builder()
                .reviewId(1L)
                .senderId(5L)
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
                .categoryName("심부름")
                .missionTitle("심부름 미션")
                .starScore(3)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
