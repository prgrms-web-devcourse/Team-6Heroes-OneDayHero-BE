package com.sixheroes.onedayheroapplication.global.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageDeleteServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageDeleteServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageDeleteService {

    @Value("${cloud.aws.s3.bucket:default}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<S3ImageDeleteServiceResponse> deleteImages(List<S3ImageDeleteServiceRequest> s3ImageDeleteServiceRequests) {
        return s3ImageDeleteServiceRequests.stream()
                .map(s3ImageDeleteServiceRequest -> {
                    deleteImage(s3ImageDeleteServiceRequest.uniqueName());
                    return new S3ImageDeleteServiceResponse(s3ImageDeleteServiceRequest.imageId());
                }).toList();
    }

    private void deleteImage(String uniqueName) {
        amazonS3.deleteObject(
                bucket,
                uniqueName
        );
    }
}
