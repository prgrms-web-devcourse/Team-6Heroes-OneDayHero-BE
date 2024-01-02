package com.sixheroes.onedayheroapi.user;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.region.response.RegionResponse;
import com.sixheroes.onedayheroapplication.user.ProfileService;
import com.sixheroes.onedayheroapplication.user.request.HeroRankServiceRequest;
import com.sixheroes.onedayheroapplication.user.response.*;
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
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
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
    private ProfileService profileService;

    @Override
    protected Object setController() {
        return new ProfileController(profileService);
    }

    @DisplayName("유저의 시민 프로필을 조회한다.")
    @Test
    void findCitizenProfile() throws Exception {
        // given
        var userId = 1L;

        var userBasicInfoResponse = new ProfileCitizenResponse.UserBasicInfoForProfileCitizenResponse("이름", "MALE", LocalDate.of(1990, 1, 1));
        var userImageResponse = new UserImageResponse(1L, "profile.jpg", "unique.jpg", "https://");
        var heroScore = 60;

        var profileCitizenResponse = new ProfileCitizenResponse(userBasicInfoResponse, userImageResponse, heroScore);

        given(profileService.findCitizenProfile(anyLong())).willReturn(profileCitizenResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/{userId}/citizen-profile", userId)
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
                    fieldWithPath("data.image.id").type(JsonFieldType.NUMBER)
                        .optional()
                        .description("이미지 아이디"),
                    fieldWithPath("data.image.originalName").type(JsonFieldType.STRING)
                        .optional()
                        .description("원본 이름"),
                    fieldWithPath("data.image.uniqueName").type(JsonFieldType.STRING)
                        .optional()
                        .description("고유 이름"),
                    fieldWithPath("data.image.path").type(JsonFieldType.STRING)
                        .optional()
                        .description("이미지 경로"),
                    fieldWithPath("data.heroScore").type(JsonFieldType.NUMBER).description("히어로 점수")
                )
            ));
    }

    @DisplayName("유저의 히어로 프로필을 조회한다.")
    @Test
    void findHeroProfile() throws Exception {
        // given
        var userId = 1L;

        var profileHeroResponse = createProfileHeroResponse();

        given(profileService.findHeroProfile(anyLong())).willReturn(profileHeroResponse);

        // when & then
        mockMvc.perform(get("/api/v1/users/{userId}/hero-profile", userId)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.basicInfo").exists())
            .andExpect(jsonPath("$.data.basicInfo.nickname").value(profileHeroResponse.basicInfo().nickname()))
            .andExpect(jsonPath("$.data.basicInfo.gender").value(profileHeroResponse.basicInfo().gender()))
            .andExpect(jsonPath("$.data.basicInfo.birth").value(DateTimeConverter.convertDateToString(profileHeroResponse.basicInfo().birth())))
            .andExpect(jsonPath("$.data.basicInfo.introduce").value(profileHeroResponse.basicInfo().introduce()))
            .andExpect(jsonPath("$.data.image").exists())
            .andExpect(jsonPath("$.data.image.originalName").value(profileHeroResponse.image().originalName()))
            .andExpect(jsonPath("$.data.image.uniqueName").value(profileHeroResponse.image().uniqueName()))
            .andExpect(jsonPath("$.data.image.path").value(profileHeroResponse.image().path()))
            .andExpect(jsonPath("$.data.favoriteWorkingDay").exists())
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteDate").isArray())
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteDate.[0]").value(profileHeroResponse.favoriteWorkingDay().favoriteDate().get(0)))
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteStartTime").value(DateTimeConverter.convertTimetoString(profileHeroResponse.favoriteWorkingDay().favoriteStartTime())))
            .andExpect(jsonPath("$.data.favoriteWorkingDay.favoriteEndTime").value(DateTimeConverter.convertTimetoString(profileHeroResponse.favoriteWorkingDay().favoriteEndTime())))
            .andExpect(jsonPath("$.data.favoriteRegions").exists())
            .andExpect(jsonPath("$.data.favoriteRegions[0].id").value(profileHeroResponse.favoriteRegions().get(0).id()))
            .andExpect(jsonPath("$.data.favoriteRegions[0].si").value(profileHeroResponse.favoriteRegions().get(0).si()))
            .andExpect(jsonPath("$.data.favoriteRegions[0].gu").value(profileHeroResponse.favoriteRegions().get(0).gu()))
            .andExpect(jsonPath("$.data.favoriteRegions[0].dong").value(profileHeroResponse.favoriteRegions().get(0).dong()))
            .andExpect(jsonPath("$.data.heroScore").value(profileHeroResponse.heroScore()))
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
                    fieldWithPath("data.image.id").type(JsonFieldType.NUMBER)
                        .optional()
                        .description("이미지 아이디"),
                    fieldWithPath("data.image.originalName")
                        .type(JsonFieldType.STRING)
                        .optional()
                        .description("원본 이름"),
                    fieldWithPath("data.image.uniqueName")
                        .type(JsonFieldType.STRING)
                        .optional()
                        .description("고유 이름"),
                    fieldWithPath("data.image.path")
                        .type(JsonFieldType.STRING)
                        .optional()
                        .description("이미지 경로"),
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
                    fieldWithPath("data.heroScore").type(JsonFieldType.NUMBER).description("히어로 점수")
                )
            ));
    }

    @DisplayName("히어로를 닉네임으로 검색한다.")
    @Test
    void searchHeroes() throws Exception {
        // given
        var nickname = "님";
        var pageRequest = PageRequest.of(0, 3);
        var heroSearchResponses = createHeroSearchResponses();
        var slice = new SliceImpl<HeroSearchResponse>(heroSearchResponses, pageRequest, true);

        given(profileService.searchHeroes(anyString(), any(Pageable.class))).willReturn(slice);

        // when & then
        mockMvc.perform(get("/api/v1/users/hero-search")
                .param("page", "0")
                .param("size", "3")
                .param("nickname", nickname)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.content[0].id").value(slice.getContent().get(0).id()))
            .andExpect(jsonPath("$.data.content[0].nickname").value(slice.getContent().get(0).nickname()))
            .andExpect(jsonPath("$.data.content[0].image.originalName").value(slice.getContent().get(0).image().originalName()))
            .andExpect(jsonPath("$.data.content[0].image.uniqueName").value(slice.getContent().get(0).image().uniqueName()))
            .andExpect(jsonPath("$.data.content[0].image.path").value(slice.getContent().get(0).image().path()))
            .andExpect(jsonPath("$.data.content[0].favoriteMissionCategories").isArray())
            .andExpect(jsonPath("$.data.content[0].favoriteMissionCategories[0].code").value(slice.getContent().get(0).favoriteMissionCategories().get(0).code()))
            .andExpect(jsonPath("$.data.content[0].favoriteMissionCategories[0].name").value(slice.getContent().get(0).favoriteMissionCategories().get(0).name()))
            .andExpect(jsonPath("$.data.content[0].heroScore").value(slice.getContent().get(0).heroScore()))
            .andDo(document("hero-nickname-search",
                queryParameters(
                    parameterWithName("page").optional()
                        .description("페이지 번호"),
                    parameterWithName("size").optional()
                        .description("데이터 크기"),
                    parameterWithName("nickname")
                        .description("히어로 이름")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                        .description("히어로 목록 배열"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                        .description("히어로 아이디"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING)
                        .description("히어로 이름"),
                    fieldWithPath("data.content[].image.originalName").type(JsonFieldType.STRING)
                        .description("이미지 원본 이름")
                        .optional(),
                    fieldWithPath("data.content[].image.id").type(JsonFieldType.NUMBER)
                        .optional()
                        .description("이미지 아이디"),
                    fieldWithPath("data.content[].image.uniqueName").type(JsonFieldType.STRING)
                        .description("이미지 고유 이름")
                        .optional(),
                    fieldWithPath("data.content[].image.path").type(JsonFieldType.STRING)
                        .description("이미지 경로")
                        .optional(),
                    fieldWithPath("data.content[].favoriteMissionCategories").type(JsonFieldType.ARRAY)
                        .description("선호 미션 카테고리")
                        .optional(),
                    fieldWithPath("data.content[].favoriteMissionCategories[].code").type(JsonFieldType.STRING)
                        .description("선호 미션 카테고리 코드"),
                    fieldWithPath("data.content[].favoriteMissionCategories[].name").type(JsonFieldType.STRING)
                        .description("선호 미션 카테고리 이름"),
                    fieldWithPath("data.content[].heroScore").type(JsonFieldType.NUMBER)
                        .description("히어로 점수"),
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

    @DisplayName("지역별, 카테고리별 히어로 랭킹을 조회한다.")
    @Test
    void findHeroesRank() throws Exception {
        // given
        var region = "역삼동";
        var missionCategoryCode = "MC_001";

        var pageRequest = PageRequest.of(0, 3);
        var heroRankResponses = createHeroRankResponses();
        var slice = new SliceImpl<HeroRankResponse>(heroRankResponses, pageRequest, true);

        given(profileService.findHeroesRank(any(HeroRankServiceRequest.class), any(Pageable.class))).willReturn(slice);

        // when & then
        mockMvc.perform(get("/api/v1/users/hero-rank")
                .param("page", "0")
                .param("size", "3")
                .param("region", region)
                .param("missionCategoryCode", missionCategoryCode)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.content[0].id").value(slice.getContent().get(0).id()))
            .andExpect(jsonPath("$.data.content[0].nickname").value(slice.getContent().get(0).nickname()))
            .andExpect(jsonPath("$.data.content[0].profileImagePath").value(slice.getContent().get(0).profileImagePath()))
            .andExpect(jsonPath("$.data.content[0].rank").value(slice.getContent().get(0).rank()))
            .andExpect(jsonPath("$.data.content[0].heroScore").value(slice.getContent().get(0).heroScore()))
            .andExpect(jsonPath("$.data.content[0].favoriteMissionCategories").isArray())
            .andExpect(jsonPath("$.data.content[0].favoriteMissionCategories[0].code").value(slice.getContent().get(0).favoriteMissionCategories().get(0).code()))
            .andExpect(jsonPath("$.data.content[0].favoriteMissionCategories[0].name").value(slice.getContent().get(0).favoriteMissionCategories().get(0).name()))

            .andDo(document("hero-rank-find",
                queryParameters(
                    parameterWithName("page").optional()
                        .description("페이지 번호"),
                    parameterWithName("size").optional()
                        .description("데이터 크기"),
                    parameterWithName("region")
                        .description("지역 동 이름"),
                    parameterWithName("missionCategoryCode").optional()
                        .description("미션 카테고리 코드")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                        .description("히어로 목록 배열"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.NUMBER)
                        .description("히어로 아이디"),
                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING)
                        .description("히어로 이름"),
                    fieldWithPath("data.content[].profileImagePath").type(JsonFieldType.STRING)
                        .description("프로필 이미지 경로")
                        .optional(),
                    fieldWithPath("data.content[].heroScore").type(JsonFieldType.NUMBER)
                        .description("히어로 점수"),
                    fieldWithPath("data.content[].rank").type(JsonFieldType.NUMBER)
                            .description("히어로 랭킹"),
                    fieldWithPath("data.content[].favoriteMissionCategories").type(JsonFieldType.ARRAY)
                            .description("히어로 선호 카테고리들"),
                    fieldWithPath("data.content[].favoriteMissionCategories[].code").type(JsonFieldType.STRING)
                            .description("카테고리 코드"),
                    fieldWithPath("data.content[].favoriteMissionCategories[].name").type(JsonFieldType.STRING)
                            .description("카테고리 이름"),
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

    private List<HeroRankResponse> createHeroRankResponses() {
        var missionCategory1 = new HeroRankResponse.MissionCategoryResponse("MC_001", "서빙");
        var missionCategory2 = new HeroRankResponse.MissionCategoryResponse("MC_002", "주방");
        var heroRankResponse1 = HeroRankResponse.builder()
            .id(2L)
            .nickname("철수")
            .heroScore(60)
            .profileImagePath("https://aws.s3")
            .rank(1)
            .favoriteMissionCategories(List.of(missionCategory1, missionCategory2))
            .build();

        var heroRankResponse2 = HeroRankResponse.builder()
            .id(3L)
            .nickname("영희")
            .heroScore(50)
            .profileImagePath("https://aws.s3")
            .rank(2)
            .favoriteMissionCategories(List.of(missionCategory1, missionCategory2))
            .build();

        var heroRankResponse3 = HeroRankResponse.builder()
            .id(7L)
            .nickname("자바")
            .heroScore(40)
            .profileImagePath("https://aws.s3")
            .rank(3)
            .favoriteMissionCategories(List.of(missionCategory1, missionCategory2))
            .build();

        return List.of(heroRankResponse1, heroRankResponse2, heroRankResponse3);
    }

    private List<HeroSearchResponse> createHeroSearchResponses() {
        var hero1 = HeroSearchResponse.builder()
            .id(2L)
            .nickname("달님")
            .favoriteMissionCategories(List.of(new HeroSearchResponse.MissionCategoryResponse("MC_001", "서빙")))
            .heroScore(30)
            .image(createUserImageResponse())
            .build();

        var hero2 = HeroSearchResponse.builder()
            .id(3L)
            .nickname("별님")
            .favoriteMissionCategories(List.of(new HeroSearchResponse.MissionCategoryResponse("MC_002", "주방")))
            .heroScore(30)
            .image(createUserImageResponse())
            .build();

        var hero3 = HeroSearchResponse.builder()
            .id(1L)
            .nickname("햇님")
            .favoriteMissionCategories(Collections.emptyList())
            .heroScore(30)
            .image(createUserImageResponse())
            .build();

        return List.of(hero1, hero2, hero3);
    }

    private ProfileHeroResponse createProfileHeroResponse() {
        return ProfileHeroResponse.builder()
            .basicInfo(createUserBasicInfoResponse())
            .image(createUserImageResponse())
            .favoriteWorkingDay(createUserFavoriteWorkingDayResponse())
            .favoriteRegions(createRegionResponses())
            .heroScore(30)
            .heroScore(60)
            .build();
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
        return new UserImageResponse(1L, "profile.jpg", "unique.jpg", "https://");
    }

    private UserFavoriteWorkingDayResponse createUserFavoriteWorkingDayResponse() {
        return new UserFavoriteWorkingDayResponse(List.of("MON", "THU"), LocalTime.of(12, 0, 0), LocalTime.of(18, 0, 0));
    }
}
