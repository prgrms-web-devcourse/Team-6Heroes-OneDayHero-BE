package com.sixheroes.onedayheroapplication.oauth.kakao;

import com.sixheroes.onedayheroapplication.oauth.OauthProperties;
import com.sixheroes.onedayheroinfraopenfeign.feign.client.oauth.kakao.KakaoAuthServerFeignClient;
import com.sixheroes.onedayheroinfraopenfeign.feign.client.oauth.kakao.KakaoResourceServerFeignClient;
import com.sixheroes.onedayheroapplication.oauth.OauthClientService;
import com.sixheroes.onedayherodomain.user.UserSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthClientService implements OauthClientService {

    private final OauthProperties oauthKakaoProperties;
    private final KakaoAuthServerFeignClient kakaoAuthServerFeignClient;
    private final KakaoResourceServerFeignClient kakaoResourceServerFeignClient;

    @Override
    public String getSocialType() {
        return oauthKakaoProperties.getKakao()
                .getAuthorizationServer();
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
                "client_id", oauthKakaoProperties.getKakao().getClientId(),
                "redirect_uri", oauthKakaoProperties.getKakao().getRedirectUri(),
                "code", code,
                "grant_type", "authorization_code"
        );
    }}
