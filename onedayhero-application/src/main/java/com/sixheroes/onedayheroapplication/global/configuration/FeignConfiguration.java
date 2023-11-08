package com.sixheroes.onedayheroapplication.global.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.sixheroes.onedayheroapplication.global.feign")
@Configuration
public class FeignConfiguration {
}
