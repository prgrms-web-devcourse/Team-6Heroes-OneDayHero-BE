package com.sixheroes.onedayheroapplication.global.oauth;


public interface OauthClientService {

    String getSocialType();

    String requestToken(String code);

    String requestResource(String accessToken);
}
