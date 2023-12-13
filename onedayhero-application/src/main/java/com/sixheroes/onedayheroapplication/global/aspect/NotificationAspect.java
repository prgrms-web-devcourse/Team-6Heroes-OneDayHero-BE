package com.sixheroes.onedayheroapplication.global.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotificationAspect {

    @Pointcut("bean(*Service)")
    private void service() {

    }
}