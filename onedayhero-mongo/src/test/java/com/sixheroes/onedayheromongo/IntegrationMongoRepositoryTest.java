package com.sixheroes.onedayheromongo;

import com.sixheroes.onedayheromongo.alarm.mongo.AlarmRepository;
import com.sixheroes.onedayheromongo.alarm.mongo.AlarmTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest
@Import(IntegrationMongoRepositoryTest.IntegrationMongoTestConfiguration.class)
public abstract class IntegrationMongoRepositoryTest {

    @Autowired
    protected AlarmRepository alarmRepository;

    @Autowired
    protected AlarmTemplateRepository alarmTemplateRepository;

    @TestConfiguration
    static class IntegrationMongoTestConfiguration {

        @Bean(initMethod = "start", destroyMethod = "stop")
        public MongoDBContainer mongoDBContainer() {
            return new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
        }

        @Bean
        @DependsOn("mongoDBContainer")
        public MongoDatabaseFactory mongoDatabaseFactory(
            final MongoDBContainer mongoDBContainer
        ) {
            return new SimpleMongoClientDatabaseFactory(mongoDBContainer.getReplicaSetUrl());
        }
    }
}