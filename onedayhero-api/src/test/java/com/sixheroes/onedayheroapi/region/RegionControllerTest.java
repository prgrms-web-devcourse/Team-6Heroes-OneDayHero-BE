package com.sixheroes.onedayheroapi.region;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.region.RegionService;
import com.sixheroes.onedayheroapplication.region.response.AllRegionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegionController.class)
public class RegionControllerTest extends RestDocsSupport {

    @MockBean
    private RegionService regionService;

    @Override
    protected Object setController() {
        return new RegionController(regionService);
    }

    @DisplayName("전체 지역을 조회 할 수 있다.")
    @Test
    void findAllRegions() throws Exception {
        // given
        var gangnamGuDongList = List.of(AllRegionResponse.DongResponse.builder()
                        .id(1L)
                        .dong("삼성1동")
                        .build(),
                AllRegionResponse.DongResponse.builder()
                        .id(2L)
                        .dong("삼성2동")
                        .build());

        var seochoGuDongList = List.of(AllRegionResponse.DongResponse.builder()
                        .id(3L)
                        .dong("양재1동")
                        .build(),
                AllRegionResponse.DongResponse.builder()
                        .id(4L)
                        .dong("양재2동")
                        .build());

        var guResponse = List.of(AllRegionResponse.GuResponse.builder()
                        .gu("강남구")
                        .dong(gangnamGuDongList)
                        .build(),
                AllRegionResponse.GuResponse.builder()
                        .gu("서초구")
                        .dong(seochoGuDongList)
                        .build());

        var regionResponse = AllRegionResponse.builder()
                .si("서울특별시")
                .gu(guResponse)
                .build();

        var result = List.of(regionResponse);

        given(regionService.findAllRegions())
                .willReturn(result);

        // when & then
        mockMvc.perform(get("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("region-findAll",
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data[].si").type(JsonFieldType.STRING)
                                        .description("행정 구역 (시)"),
                                fieldWithPath("data[].gu").type(JsonFieldType.ARRAY)
                                        .description("행정 구역 (구) 배열"),
                                fieldWithPath("data[].gu[].gu").type(JsonFieldType.STRING)
                                        .description("행정 구역 (구)"),
                                fieldWithPath("data[].gu[].dong").type(JsonFieldType.ARRAY)
                                        .description("행정 구역 (동) 배열"),
                                fieldWithPath("data[].gu[].dong[].id").type(JsonFieldType.NUMBER)
                                        .description("지역 아이디"),
                                fieldWithPath("data[].gu[].dong[].dong").type(JsonFieldType.STRING)
                                        .description("행정 구역 (동)"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].si").value(result.get(0).si()))
                .andExpect(jsonPath("$.data[0].gu[0].gu").value(result.get(0).gu().get(0).gu()))
                .andExpect(jsonPath("$.data[0].gu[0].dong[0].id").value(result.get(0).gu().get(0).dong().get(0).id()))
                .andExpect(jsonPath("$.data[0].gu[0].dong[0].dong").value(result.get(0).gu().get(0).dong().get(0).dong()))
                .andExpect(jsonPath("$.serverDateTime").exists());
    }
}
