package com.sixheroes.onedayheroinfrachat.configuration;

import com.sixheroes.onedayheroinfrachat.repository.CustomMissionChatRoomRepository;
import com.sixheroes.onedayheroinfrachat.repository.MemoryChatRoomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ChatRoomRepositoryConfiguration {

    @Profile({"dev", "test"})
    @Bean
    public CustomMissionChatRoomRepository customMissionChatRoomRepository() {
        return new MemoryChatRoomRepository();
    }
}
