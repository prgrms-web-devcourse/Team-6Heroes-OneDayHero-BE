package com.sixheroes.onedayheroinfrachat.configuration;

import com.sixheroes.onedayheroinfrachat.repository.MemoryWebSocketSessionRepository;
import com.sixheroes.onedayheroinfrachat.repository.SocketSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketBeanConfiguration {

    @Bean
    public SocketSessionRepository socketSessionRepository() {
        return new MemoryWebSocketSessionRepository();
    }
}
