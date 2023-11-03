package com.sixheroes.onedayheroapi.global.jwt;

public final class AuthContextHolder {

    private static final ThreadLocal<Long> authContextHolder = new ThreadLocal<>();

    public static void clearContext() {
        authContextHolder.remove();
    }

    public static Long getContext() {
        return authContextHolder.get();
    }

    public static void setContext(Long id) {
        authContextHolder.set(id);
    }
}
