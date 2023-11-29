package com.sixheroes.onedayheroapplication.global.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sixheroes.onedayheroapplication.global.aws.AmazonProperties;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageDirectoryProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(S3ImageDirectoryProperties.class)
@Configuration
public class S3Configuration {

    private final AmazonProperties amazonProperties;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(amazonProperties.getAccessKey(), amazonProperties.getSecretKey());

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(amazonProperties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
