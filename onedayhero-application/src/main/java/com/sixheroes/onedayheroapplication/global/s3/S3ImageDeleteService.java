package com.sixheroes.onedayheroapplication.global.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageDeleteRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageDeleteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("!test")
@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageDeleteService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<S3ImageDeleteResponse> deleteImages(List<S3ImageDeleteRequest> s3ImageDeleteRequests) {
        return s3ImageDeleteRequests.stream()
                .map(s3ImageDeleteRequest -> {
                    deleteImage(s3ImageDeleteRequest.uniqueName());
                    return new S3ImageDeleteResponse(s3ImageDeleteRequest.imageId());
                }).toList();
    }

    private void deleteImage(String uniqueName) {
        amazonS3.deleteObject(
                bucket,
                uniqueName
        );
    }
}
