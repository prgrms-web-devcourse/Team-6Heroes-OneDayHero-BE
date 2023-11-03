package com.sixheroes.onedayheroapi.global.jwt;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

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
        return AuthContextHolder.getContext();
    }
}
