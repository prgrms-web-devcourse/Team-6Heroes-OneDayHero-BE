package com.sixheroes.onedayheroapplication.global.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sixheroes.onedayheroapplication.global.s3.S3ImageDirectoryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@RequiredArgsConstructor
@EnableConfigurationProperties(S3ImageDirectoryProperties.class)
@Configuration
public class S3Configuration {

    @Value("${cloud.aws.credentials.access-key:default}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:default}")
    private String secretKey;

    @Value("${cloud.aws.region.static:default}")
    private String region;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        return (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
