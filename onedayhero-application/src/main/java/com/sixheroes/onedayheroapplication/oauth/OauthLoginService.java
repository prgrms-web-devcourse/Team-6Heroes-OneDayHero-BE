package com.sixheroes.onedayheroapplication.oauth;

import com.sixheroes.onedayheroapplication.user.response.UserAuthResponse;

public interface OauthLoginService {

    Boolean supports(String authorizationServer);

    UserAuthResponse login(String code);
}
