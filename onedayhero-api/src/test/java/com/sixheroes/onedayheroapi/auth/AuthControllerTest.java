package com.sixheroes.onedayheroapi.auth;

import com.sixheroes.onedayheroapi.auth.response.oauth.LoginRequest;
import com.sixheroes.onedayheroapi.docs.OauthTestConfiguration;
import com.sixheroes.onedayheroapi.docs.RestDocsSupport;
import com.sixheroes.onedayheroapplication.auth.infra.RefreshTokenGenerator;
import com.sixheroes.onedayheroapplication.oauth.OauthLoginFacadeService;
import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroapplication.oauth.response.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sixheroes.onedayheroapi.docs.DocumentFormatGenerator.getDateTimeFormat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(OauthTestConfiguration.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest extends RestDocsSupport {

    @Autowired
    private OauthProperties oauthProperties;

    @MockBean
    private OauthLoginFacadeService oauthLoginFacadeService;

    @Override
    protected Object setController() {
        return new AuthController(oauthProperties, oauthLoginFacadeService);
    }

    @DisplayName("카카오 로그인 테스트")
    @Test
    void kakaoLogin() throws Exception {
        // given
        var refreshToken = RefreshTokenGenerator.generate();
        var request = new LoginRequest("authorization code");
        var response = new LoginResponse(1L, "accessToken", refreshToken, false);
        given(oauthLoginFacadeService.login(anyString(), anyString())).willReturn(response);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                .andDo(document("kakao-login",
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("인가 코드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE)
                                        .description("발급된 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("로그인 유저 아이디"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("발급된 액세스 토큰"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(getDateTimeFormat())
                        )));
    }

    @DisplayName("카카오 회원가입 테스트")
    @Test
    void kakaoSignup() throws Exception {
        // given
        var refreshToken = RefreshTokenGenerator.generate();
        var request = new LoginRequest("authorization code");
        var response = new LoginResponse(1L, "accessToken", refreshToken, true);
        given(oauthLoginFacadeService.login(anyString(), anyString())).willReturn(response);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId").value(response.userId()))
                .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                .andDo(document("kakao-signUp",
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING)
                                        .description("인가 코드")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE)
                                        .description("발급된 리프레시 토큰")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER)
                                        .description("HTTP 응답 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                        .description("회원가입 후 로그인한 유저 아이디"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("발급된 액세스 토큰"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING)
                                        .description("서버 응답 시간").attributes(getDateTimeFormat())
                        )));
    }
}
