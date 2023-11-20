package com.sixheroes.onedayherochat.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Configuration
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(
            MessageBrokerRegistry registry
    ) {
        // broker에 1 : 1 관계라면 /queue 를, 1 : N 관계라면 /topic 을 사용한다.
        // 구독자를 지정
        registry.enableSimpleBroker("/queue");

        // 발행자를 지정한다. 클라이언트에서 /chat/xxxx 를 사용하면 controller가 메시지를 받아 처리한다.
        registry.setApplicationDestinationPrefixes("/pub");
    }

    /**
     * @addEndpoint : 소켓 연결 uri 를 지정한다.
     * @setAllowedOriginPatterns : cors 설정, 향후 localhost:80, localhost:443 으로 변경
     * @withSockJs() : webSocket 을 사용 할 수 없는 브라우저에서 webSocket이 적용되는 것처럼 보이게 하는 라이브러리 적용
     * - webSocket을 지원하지 않는다면 다음과 같이 변경된다. streaming -> polling
     */
    @Override
    public void registerStompEndpoints(
            StompEndpointRegistry registry
    ) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
