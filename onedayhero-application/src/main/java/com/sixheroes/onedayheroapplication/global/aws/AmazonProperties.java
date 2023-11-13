package com.sixheroes.onedayheroapplication.global.aws;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "aws-info")
public class AmazonProperties {

    private String accessKey;
    private String secretKey;
    private String region;

    @PostConstruct
    public void init() {
        if (accessKey == null) {
            accessKey = "default";
        }

        if (secretKey == null) {
            secretKey = "default";
        }

        if (region == null) {
            region = "default";
        }
    }
}
