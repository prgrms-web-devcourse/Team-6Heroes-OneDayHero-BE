package com.sixheroes.onedayheroapplication.global.oauth.kakao;

import com.sixheroes.onedayheroapplication.global.feign.client.kakako.KakaoAuthServerFeignClient;
import com.sixheroes.onedayheroapplication.global.feign.client.kakako.KakaoResourceServerFeignClient;
import com.sixheroes.onedayheroapplication.global.oauth.OauthClientService;
import com.sixheroes.onedayherodomain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthClient implements OauthClientService {

    private final OauthKakaoProperties oauthKakaoProperties;
    private final KakaoAuthServerFeignClient kakaoAuthServerFeignClient;
    private final KakaoResourceServerFeignClient kakaoResourceServerFeignClient;

    @Override
    public String getSocialType() {
        return UserSocialType.KAKAO.name();
    }

    @Override
    public String requestToken(String code) {
        return kakaoAuthServerFeignClient
                .requestToken(getParamMap(code))
                .accessToken();
    }

    @Override
    public String requestResource(String accessToken) {
        return kakaoResourceServerFeignClient
                .userInfoRequest(plusBearerType(accessToken))
                .getEmail();
    }

    private String plusBearerType(String accessToken) {
        return "Bearer " + accessToken;
    }

    private Map<String, String> getParamMap(String code) {
        return Map.of(
                "client_id", oauthKakaoProperties.getClientId(),
                "redirect_uri", oauthKakaoProperties.getRedirectUri(),
                "code", code,
                "grant_type", "authorization_code"
        );
    }}
