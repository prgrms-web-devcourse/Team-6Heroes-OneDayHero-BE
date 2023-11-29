package com.sixheroes.onedayheroapi.alarm;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.alarm.AlarmService;
import com.sixheroes.onedayheroapplication.alarm.response.AlarmResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
class AlarmControllerTest extends RestDocsSupport {

    @MockBean
    private AlarmService alarmService;

    @Override
    protected Object setController() {
        return new AlarmController(alarmService);
    }

    @DisplayName("특정 유저의 알람을 최신 순으로 조회한다.")
    @Test
    void findAll() throws Exception {
        // given
        var alarmResponses = createdAlarm();
        var pageRequest = PageRequest.of(1, 4);

        var slice = new SliceImpl<AlarmResponse>(alarmResponses, pageRequest, true);

        given(alarmService.findAlarm(anyLong(), any(Pageable.class))).willReturn(slice);

        // when & then
        mockMvc.perform(get("/api/v1/alarms")
            .header(HttpHeaders.AUTHORIZATION, getAccessToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.content[0].id").value(alarmResponses.get(0).id()))
            .andExpect(jsonPath("$.data.content[0].title").value(alarmResponses.get(0).title()))
            .andExpect(jsonPath("$.data.content[0].content").value(alarmResponses.get(0).content()))
            .andExpect(jsonPath("$.data.content[0].createdAt").value(DateTimeConverter.convertLocalDateTimeToString(alarmResponses.get(0).createdAt())))
            .andExpect(jsonPath("$.data.pageable.pageNumber").value(slice.getPageable().getPageNumber()))
            .andExpect(jsonPath("$.data.pageable.pageSize").value(slice.getPageable().getPageSize()))
            .andExpect(jsonPath("$.data.pageable.sort.empty").value(slice.getPageable().getSort().isEmpty()))
            .andExpect(jsonPath("$.data.pageable.offset").value(slice.getPageable().getOffset()))
            .andExpect(jsonPath("$.data.pageable.paged").value(slice.getPageable().isPaged()))
            .andExpect(jsonPath("$.data.pageable.unpaged").value(slice.getPageable().isUnpaged()))
            .andExpect(jsonPath("$.data.size").value(slice.getSize()))
            .andExpect(jsonPath("$.data.number").value(slice.getNumber()))
            .andExpect(jsonPath("$.data.sort.empty").value(slice.getSort().isEmpty()))
            .andExpect(jsonPath("$.data.sort.sorted").value(slice.getSort().isSorted()))
            .andExpect(jsonPath("$.data.sort.unsorted").value(slice.getSort().isUnsorted()))
            .andExpect(jsonPath("$.data.numberOfElements").value(slice.getNumberOfElements()))
            .andExpect(jsonPath("$.data.first").value(slice.isFirst()))
            .andExpect(jsonPath("$.data.last").value(slice.isLast()))
            .andExpect(jsonPath("$.data.empty").value(slice.isEmpty()))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andDo(document("alarm-find",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                queryParameters(
                    parameterWithName("page").optional()
                        .description("페이지 번호"),
                    parameterWithName("size").optional()
                        .description("데이터 크기")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.content[]").type(JsonFieldType.ARRAY)
                        .description("알람 응답 데이터 배열"),
                    fieldWithPath("data.content[].id").type(JsonFieldType.STRING)
                        .description("알람 ID"),
                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                        .description("알람 제목"),
                    fieldWithPath("data.content[].content")
                        .description("알람 내용"),
                    fieldWithPath("data.content[].createdAt")
                        .description("알림 생성 시간"),
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
                        .description("알람 리스트 크기"),
                    fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                        .description("현재 페이지 번호"),
                    fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                        .description("알람 리스트 정렬 정보 객체"),
                    fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                        .description("알람 리스트의 정렬 정보가 비어있는지 여부"),
                    fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                        .description("알람 리스트의 정렬 정보가 있는지 여부"),
                    fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                        .description("알람 리스트의 정렬 정보가 정렬되지 않은지 여부"),
                    fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                        .description("현재 페이지의 요소 수"),
                    fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                        .description("첫 번째 페이지인지 여부"),
                    fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                        .description("마지막 페이지인지 여부"),
                    fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                        .description("알람 리스트가 비어있는지 여부"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .attributes(getDateTimeFormat())
                        .description("서버 응답 시간")
                )
            ));
    }

    @DisplayName("알람을 삭제한다")
    @Test
    void deleteAlarm() throws Exception {
        // given
        var alarmId = UUID.randomUUID().toString();

        doNothing().when(alarmService).deleteAlarm(anyLong(), anyString());

        // when
        mockMvc.perform(delete("/api/v1/alarms/{alarmId}", alarmId)
            .header(HttpHeaders.AUTHORIZATION, getAccessToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.status").value(204))
            .andExpect(jsonPath("$.serverDateTime").exists())
            .andDo(document("alarm-delete",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                ),
                pathParameters(
                    parameterWithName("alarmId").description("알람 아이디")
                ),
                responseFields(
                    fieldWithPath("status").type(JsonFieldType.NUMBER)
                        .description("HTTP 응답 코드"),
                    fieldWithPath("data").type(JsonFieldType.NULL)
                        .description("응답 데이터"),
                    fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                        .attributes(getDateTimeFormat())
                        .description("서버 응답 시간")
                )
            ));
    }

    private List<AlarmResponse> createdAlarm() {
        var time = LocalDateTime.of(2023, 11, 21, 12, 0, 0);

        var alarmResponse1 = AlarmResponse.builder()
            .id(UUID.randomUUID().toString())
            .title("알림 제목")
            .content("알림 내용")
            .createdAt(time)
            .build();

        var alarmResponse2 = AlarmResponse.builder()
            .id(UUID.randomUUID().toString())
            .title("알림 제목")
            .content("알림 내용")
            .createdAt(time.minusHours(2))
            .build();

        var alarmResponse3 = AlarmResponse.builder()
            .id(UUID.randomUUID().toString())
            .title("알림 제목")
            .content("알림 내용")
            .createdAt(time.minusDays(1))
            .build();

        var alarmResponse4 = AlarmResponse.builder()
            .id(UUID.randomUUID().toString())
            .title("알림 제목")
            .content("알림 내용")
            .createdAt(time.minusWeeks(1))
            .build();

        return List.of(alarmResponse1, alarmResponse2, alarmResponse3, alarmResponse4);
    }
}