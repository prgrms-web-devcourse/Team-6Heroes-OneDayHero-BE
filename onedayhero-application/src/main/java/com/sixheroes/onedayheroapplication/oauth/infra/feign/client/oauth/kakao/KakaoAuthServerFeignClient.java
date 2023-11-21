package com.sixheroes.onedayheroapplication.oauth.infra.feign.client.oauth.kakao;

import com.sixheroes.onedayheroapplication.oauth.infra.configuration.KakaoFeignConfiguration;
import com.sixheroes.onedayheroapplication.oauth.infra.feign.client.oauth.kakao.response.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;


@FeignClient(
        name = "kakao-auth",
        url = "https://kauth.kakao.com/oauth/token",
        configuration = KakaoFeignConfiguration.class
)
public interface KakaoAuthServerFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponse requestToken(Map<String, ?> data);
}


