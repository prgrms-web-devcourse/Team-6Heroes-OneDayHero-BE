package com.sixheroes.onedayheroapi.review;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.review.request.ReviewCreateRequest;
import com.sixheroes.onedayheroapplication.mission.response.MissionCategoryResponse;
import com.sixheroes.onedayheroapplication.review.ReviewService;
import com.sixheroes.onedayheroapplication.review.reqeust.ReviewCreateServiceRequest;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewResponse;
import com.sixheroes.onedayheroapplication.review.response.ReceivedReviewViewResponse;
import com.sixheroes.onedayheroapplication.review.response.ReviewResponse;
import com.sixheroes.onedayherocommon.converter.DateTimeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReviewController.class)
class ReviewControllerTest extends RestDocsSupport {

    @MockBean
    private ReviewService reviewService;

    @Override
    protected Object setController() {
        return new ReviewController(reviewService);
    }

    @DisplayName("리뷰 상세 조회를 할 수 있다")
    @Test
    void findReviewDetail() throws Exception {
        // given
        var senderId = 1L;
        var receiverId = 2L;
        var missionTitle = "서빙 구함";
        var content = "리뷰 내용";
        var starScore = 5;
        var createdAt = LocalDateTime.now();

        var response = createReview(
                senderId,
                receiverId,
                missionTitle,
                content,
                starScore,
                createdAt
        );
        given(reviewService.viewReviewDetail(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/reviews/{reviewId}", response.id())
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.senderId").value(response.senderId()))
                .andExpect(jsonPath("$.data.receiverId").value(response.receiverId()))
                .andExpect(jsonPath("$.data.missionCategory.id").value(response.missionCategory().id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(response.missionCategory().code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(response.missionCategory().name()))
                .andExpect(jsonPath("$.data.missionTitle").value(response.missionTitle()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.starScore").value(response.starScore()))
                .andExpect(jsonPath("$.data.reviewImageResponses.size()").value(response.reviewImageResponses().size()))
                .andDo(print())
                .andDo(document("review-detail",
                        pathParameters(
                                parameterWithName("reviewId")
                                        .description("리뷰 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("생성된 리뷰 아이디"),
                                fieldWithPath("data.senderId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 작성 유저 아이디"),
                                fieldWithPath("data.receiverId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 대상 유저 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.missionTitle").type(JsonFieldType.STRING)
                                        .description("리뷰가 발생된 미션 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("리뷰 내용"),
                                fieldWithPath("data.starScore").type(JsonFieldType.NUMBER)
                                        .description("별점"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("리뷰 생성 시간"),
                                fieldWithPath("data.reviewImageResponses[]").type(JsonFieldType.ARRAY)
                                        .description("리뷰 이미지 응답 데이터 배열"),
                                fieldWithPath("data.reviewImageResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("리뷰 이미지 아이디"),
                                fieldWithPath("data.reviewImageResponses[].uniqueName").type(JsonFieldType.STRING)
                                        .description("리뷰 이미지 유니크 네임"),
                                fieldWithPath("data.reviewImageResponses[].path").type(JsonFieldType.STRING)
                                        .description("리뷰 이미지 S3 주소"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )
                ));
    }

    @DisplayName("리뷰를 생성할 수 있다.")
    @Test
    void createReview() throws Exception {
        // given
        var senderId = 1L;
        var receiverId = 2L;
        var missionTitle = "서빙 구함";
        var content = "리뷰 내용";
        var starScore = 5;
        var createdAt = LocalDateTime.now();

        var request = createReviewCreateRequest(
                senderId,
                receiverId,
                missionTitle,
                content,
                starScore
        );
        var reviewCreateRequest = createReviewCreateRequestToMultipartFile(objectMapper.writeValueAsString(request));
        var imageA = createImageA();
        var imageB = createImageB();

        var response = createReview(
                senderId,
                receiverId,
                missionTitle,
                content,
                starScore,
                createdAt
        );
        given(reviewService.create(any(ReviewCreateServiceRequest.class), any(Optional.class))).willReturn(response);

        // when & then
        mockMvc.perform(
                multipart(HttpMethod.POST, "/api/v1/reviews")
                        .file(reviewCreateRequest)
                        .file(imageA)
                        .file(imageB)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/reviews/" + response.id()))
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.senderId").value(response.senderId()))
                .andExpect(jsonPath("$.data.receiverId").value(response.receiverId()))
                .andExpect(jsonPath("$.data.missionCategory.id").value(response.missionCategory().id()))
                .andExpect(jsonPath("$.data.missionCategory.code").value(response.missionCategory().code()))
                .andExpect(jsonPath("$.data.missionCategory.name").value(response.missionCategory().name()))
                .andExpect(jsonPath("$.data.missionTitle").value(response.missionTitle()))
                .andExpect(jsonPath("$.data.content").value(response.content()))
                .andExpect(jsonPath("$.data.starScore").value(response.starScore()))
                .andExpect(jsonPath("$.data.reviewImageResponses.size()").value(response.reviewImageResponses().size()))
                .andDo(print())
                .andDo(document("review-create",
                        requestPartFields("reviewCreateRequest",
                                fieldWithPath("senderId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 작성 유저 아이디"),
                                fieldWithPath("receiverId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 대상 유저 아이디"),
                                fieldWithPath("missionCategoryId").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("missionTitle").type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("리뷰 내용"),
                                fieldWithPath("starScore").type(JsonFieldType.NUMBER)
                                        .description("별점")
                ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                        .description("생성된 리뷰 아이디"),
                                fieldWithPath("data.senderId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 작성 유저 아이디"),
                                fieldWithPath("data.receiverId").type(JsonFieldType.NUMBER)
                                        .description("리뷰 대상 유저 아이디"),
                                fieldWithPath("data.missionCategory").type(JsonFieldType.OBJECT)
                                        .description("미션 카테고리 정보 객체"),
                                fieldWithPath("data.missionCategory.id").type(JsonFieldType.NUMBER)
                                        .description("미션 카테고리 아이디"),
                                fieldWithPath("data.missionCategory.code").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 코드"),
                                fieldWithPath("data.missionCategory.name").type(JsonFieldType.STRING)
                                        .description("미션 카테고리 내용 ex) 청소"),
                                fieldWithPath("data.missionTitle").type(JsonFieldType.STRING)
                                        .description("리뷰가 발생된 미션 제목"),
                                fieldWithPath("data.content").type(JsonFieldType.STRING)
                                        .description("리뷰 내용"),
                                fieldWithPath("data.starScore").type(JsonFieldType.NUMBER)
                                        .description("별점"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("리뷰 생성 시간"),
                                fieldWithPath("data.reviewImageResponses[]").type(JsonFieldType.ARRAY)
                                        .description("리뷰 이미지 응답 데이터 배열"),
                                fieldWithPath("data.reviewImageResponses[].id").type(JsonFieldType.NUMBER)
                                        .description("리뷰 이미지 아이디"),
                                fieldWithPath("data.reviewImageResponses[].uniqueName").type(JsonFieldType.STRING)
                                        .description("리뷰 이미지 유니크 네임"),
                                fieldWithPath("data.reviewImageResponses[].path").type(JsonFieldType.STRING)
                                        .description("리뷰 이미지 S3 주소"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .attributes(getDateTimeFormat())
                                        .description("서버 응답 시간")
                        )
        ));
    }

    @DisplayName("리뷰를 제거할 수 있다.")
    @Test
    void deleteReview() throws Exception {
        var reviewId = 1L;
        willDoNothing().given(reviewService).delete(anyLong());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/reviews/{reviewId}", reviewId))
                .andDo(document("review-delete",
                        pathParameters(
                                parameterWithName("reviewId")
                                        .description("리뷰 아이디")
                        )));
    }

    @DisplayName("특정 유저의 리뷰를 확인할 수 있다.")
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

        mockMvc.perform(get("/api/v1/reviews/users/{userId}/receive", userId)
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
                .andDo(document("user-received-reviews",
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


    private ReviewCreateRequest createReviewCreateRequest(
            Long senderId,
            Long receiverId,
            String missionTitle,
            String content,
            Integer starScore
    ) {
        return ReviewCreateRequest.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .missionCategoryId(1L)
                .missionTitle(missionTitle)
                .content(content)
                .starScore(starScore)
                .build();
    }

    private MockMultipartFile createReviewCreateRequestToMultipartFile(
            String json
    ) {
        return new MockMultipartFile(
                "reviewCreateRequest",
                "json",
                MediaType.APPLICATION_JSON.toString(),
                json.getBytes(StandardCharsets.UTF_8)
        );
    }

    private MockMultipartFile createImageA() {
        return new MockMultipartFile(
                "images",
                "imageA.jpeg",
                "image/jpeg",
                "<<jpeg data>>".getBytes()
        );
    }

    private MockMultipartFile createImageB() {
        return new MockMultipartFile(
                "images",
                "imageB.jpeg",
                "image/jpeg",
                "<<jpeg data>>".getBytes()
        );
    }

    private ReviewResponse createReview(
            Long senderId,
            Long receiverId,
            String missionTitle,
            String content,
            Integer starScore,
            LocalDateTime createdAt
    ) {
        var savedImageA = ReviewResponse.ReviewImageResponse.builder()
                .id(1L)
                .uniqueName("A")
                .path("S3 이미지 주소A")
                .build();
        var savedImageB = ReviewResponse.ReviewImageResponse.builder()
                .id(2L)
                .uniqueName("B")
                .path("S3 이미지 주소B")
                .build();

        return ReviewResponse.builder()
                .id(1L)
                .missionCategory(MissionCategoryResponse.builder().id(1L).name("서빙").code("MC_001").build())
                .senderId(senderId)
                .receiverId(receiverId)
                .missionTitle(missionTitle)
                .content(content)
                .starScore(starScore)
                .reviewImageResponses(List.of(savedImageA, savedImageB))
                .createdAt(createdAt)
                .build();
    }
}
