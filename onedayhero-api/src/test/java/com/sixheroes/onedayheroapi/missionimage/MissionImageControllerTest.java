package com.sixheroes.onedayheroapi.missionimage;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapi.mission.MissionImageController;
import com.sixheroes.onedayheroapplication.mission.MissionImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MissionImageController.class)
public class MissionImageControllerTest extends RestDocsSupport {

    @MockBean
    private MissionImageService missionImageService;

    @Override
    protected Object setController() {
        return new MissionImageController(missionImageService);
    }

    @DisplayName("미션의 이미지를 삭제 할 수 있다.")
    @Test
    void deleteMissionImage() throws Exception {
        // given
        willDoNothing().given(missionImageService).deleteImage(any(Long.class), any(Long.class));

        // when & then
        mockMvc.perform(delete("/api/v1/mission-images/{missionImageId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("mission-image-delete",
                        requestHeaders(
                                headerWithName("Authorization").description("Auth Credential")
                        ),
                        pathParameters(
                                parameterWithName("missionImageId").description("미션 이미지 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간")
                                        .attributes(getDateTimeFormat())
                        )))
                .andExpect(jsonPath("$.status").value(204))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.serverDateTime").exists());
    }
}
