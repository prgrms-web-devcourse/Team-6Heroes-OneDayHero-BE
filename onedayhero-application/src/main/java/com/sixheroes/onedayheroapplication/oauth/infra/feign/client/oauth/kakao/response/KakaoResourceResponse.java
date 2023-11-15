package com.sixheroes.onedayheroapplication.oauth.infra.feign.client.oauth.kakao.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResourceResponse {

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {

        @JsonProperty("email")
        private String email;
    }

    public String getEmail() {
        return kakaoAccount.email;
    }
}
