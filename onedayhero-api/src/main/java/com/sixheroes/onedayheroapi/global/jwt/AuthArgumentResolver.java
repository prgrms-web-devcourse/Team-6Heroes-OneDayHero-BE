package com.sixheroes.onedayheroapi.global.jwt;

import com.sixheroes.onedayherocommon.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String USER_ID = "id";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        var hasAuthAnnotation = parameter.hasParameterAnnotation(Auth.class);
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
        var id = request.getAttribute(USER_ID);

        if (ObjectUtils.isEmpty(id)) {
            throw new IllegalStateException(ErrorCode.S_001.name());
        }

        return id;
    }
}
