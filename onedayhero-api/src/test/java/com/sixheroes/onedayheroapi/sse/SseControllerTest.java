package com.sixheroes.onedayheroapi.sse;

import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SseController.class)
class SseControllerTest extends RestDocsSupport {

    @MockBean
    private SseEmitters sseEmitters;

    @Override
    protected Object setController() {
        return new SseController(sseEmitters);
    }

    @DisplayName("SSE를 구독한다.")
    @Test
    void subscribe() throws Exception {
        // given
        var defaultTimeOut = 30 * 60 * 1000L;
        var sseEmitter = new SseEmitter(defaultTimeOut);

        given(sseEmitters.add(anyLong())).willReturn(sseEmitter);

        // when & then
        mockMvc.perform(get("/api/v1/sse/subscribe")
            .header(HttpHeaders.AUTHORIZATION, getAccessToken())
            .contentType(MediaType.TEXT_EVENT_STREAM))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("sse-subscribe",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Authorization: Bearer 액세스토큰")
                )
            ));
    }
}