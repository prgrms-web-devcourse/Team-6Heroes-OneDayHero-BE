package com.sixheroes.onedayherodomain.global.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.sixheroes.onedayherodomain.mongo")
@Configuration
public class MongoConfiguration {
    
}
