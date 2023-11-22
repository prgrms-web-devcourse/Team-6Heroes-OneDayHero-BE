package com.sixheroes.onedayheromongo.global;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.sixheroes.onedayheromongo")
public class MongoConfiguration {
}
