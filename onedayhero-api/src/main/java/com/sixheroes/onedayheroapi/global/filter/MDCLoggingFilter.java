package com.sixheroes.onedayheroapi.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var requestId = ((HttpServletRequest) request).getHeader("X-RequestID");

        var traceId = requestId != null ? requestId : UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put("request_id", traceId);
        chain.doFilter(request, response);
        MDC.clear();
    }
}
