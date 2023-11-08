package com.sixheroes.onedayheroapi.global.auth;

import com.sixheroes.onedayheroapi.global.jwt.JwtProperties;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProperties jwtProperties;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        var hasAuthAnnotation = parameter.hasParameterAnnotation(AuthUser.class);
        var hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasAuthAnnotation && hasLongType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        var request = webRequest.getNativeRequest(HttpServletRequest.class);
        var id = request.getAttribute(jwtProperties.getClaimId());

        if (ObjectUtils.isEmpty(id)) {
            throw new IllegalStateException(ErrorCode.S_001.name());
        }

        return id;
    }
}
