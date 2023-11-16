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

    @Override
    protected Object setController() {
        return new ProfileController(userService);
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
}
