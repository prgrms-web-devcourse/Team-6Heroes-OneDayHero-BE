package com.sixheroes.onedayheroapplication.global.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadServiceRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadServiceResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageUploadService {

    @Value("${cloud.aws.s3.bucket:default}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<S3ImageUploadServiceResponse> uploadImages(
            List<S3ImageUploadServiceRequest> s3ImageUploadServiceRequests,
            String dir
    ) {
        if (s3ImageUploadServiceRequests.isEmpty()) {
            return Collections.emptyList();
        }

        return s3ImageUploadServiceRequests.stream()
                .map(s3ImageUploadServiceRequest -> {
                    var originalName = s3ImageUploadServiceRequest.originalName();
                    var uniqueName = createUniqueName(dir, originalName);
                    uploadImage(uniqueName, s3ImageUploadServiceRequest);

                    return new S3ImageUploadServiceResponse(originalName, uniqueName, getPath(uniqueName));
                }).toList();
    }

    private void uploadImage(
            String uniqueName,
            S3ImageUploadServiceRequest s3ImageUploadServiceRequest
    ) {
        var metadata = new ObjectMetadata();
        metadata.setContentType(s3ImageUploadServiceRequest.contentType());
        metadata.setContentLength(s3ImageUploadServiceRequest.contentSize());

        amazonS3.putObject(
                bucket,
                uniqueName,
                s3ImageUploadServiceRequest.inputStream(),
                metadata
        );
    }

    private String createUniqueName(
            String dir,
            String originalName
    ) {
        return dir + UUID.randomUUID() + extractExtension(originalName);
    }

    private String extractExtension(String originalName) {
        var boundaryIndex = originalName.indexOf(".");

        if (validateExtensionIsNotExist(boundaryIndex)) {
            throw new IllegalArgumentException(ErrorCode.T_001.name());
        }

        return originalName.substring(boundaryIndex);
    }

    private String getPath(String uniqueName) {
        return amazonS3.getUrl(
                bucket,
                uniqueName
        ).toString();
    }

    private boolean validateExtensionIsNotExist(int boundaryIndex) {
        return boundaryIndex == -1;
    }
}
