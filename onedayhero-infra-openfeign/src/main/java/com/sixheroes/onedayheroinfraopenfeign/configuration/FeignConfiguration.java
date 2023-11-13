package com.sixheroes.onedayheroinfraopenfeign.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.sixheroes.onedayheroinfraopenfeign.feign")
@Configuration
public class FeignConfiguration {
}
