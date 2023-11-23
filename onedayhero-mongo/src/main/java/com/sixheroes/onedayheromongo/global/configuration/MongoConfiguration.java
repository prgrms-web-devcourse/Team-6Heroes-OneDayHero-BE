package com.sixheroes.onedayheromongo.global.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoAuditing
@Configuration
@EnableMongoRepositories(basePackages = "com.sixheroes.onedayheromongo")
public class MongoConfiguration {
}
