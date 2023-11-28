package com.sixheroes.onedayheroapi.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final String TRACE_ID_NAME = "request_id";

    @Pointcut("bean(*Service)")
    private void service() {
    }

    @Pointcut("bean(*Controller)")
    private void controller() {
    }

    @AfterThrowing(pointcut = "service()", throwing = "exception")
    private void logException(
            JoinPoint joinPoint,
            RuntimeException exception
    ) {
        String exceptionName = exception.getClass().getSimpleName();
        String exceptionMessage = exception.getMessage();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.warn("[{}] [Exception] {} , exceptionName = {}, exceptionMessage = {}, args = {}",
                MDC.get(TRACE_ID_NAME), methodName, exceptionName, exceptionMessage, args);
    }

    @Around("controller()")
    private Object log(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {
        var beforeTime = System.currentTimeMillis();
        var methodName = joinPoint.getSignature().toShortString();
        Object result = joinPoint.proceed();
        var executeTime = System.currentTimeMillis() - beforeTime;
        log.debug("[{}] methodName : [{}] time = [{}ms]", MDC.get(TRACE_ID_NAME), methodName, executeTime);
        return result;
    }

}
