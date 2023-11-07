package com.sixheroes.onedayheroapi.global.interceptor;

import com.sixheroes.onedayheroapi.global.jwt.JwtProperties;
import com.sixheroes.onedayheroapi.global.jwt.JwtTokenManager;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        // 존재하지 않은 URL 로 접근할 경우.
        if (!(handler instanceof HandlerMethod)) {
            response.setStatus(404);
            return false;
        }

        var authorizationHeader = getAuthorization(request);
        if (validateAuthorizationHeaderIsValid(authorizationHeader)) {
            throw new IllegalStateException(ErrorCode.A_001.name());
        }

        var id = jwtTokenManager.getId(getAccessToken(authorizationHeader));
        request.setAttribute(jwtProperties.getClaimID(), id);

        return true;
    }

    private boolean validateAuthorizationHeaderIsValid(String header) {
        return header == null || !header.startsWith("Bearer ");
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

    private String getAccessToken(String header) {
        return header.split(" ")[1].trim();
    }
}
