package com.sixheroes.onedayheroapplication.global.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadRequest;
import com.sixheroes.onedayheroapplication.global.s3.dto.response.S3ImageUploadResponse;
import com.sixheroes.onedayherocommon.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageUploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<S3ImageUploadResponse> uploadImages(
            List<S3ImageUploadRequest> s3ImageUploadRequests,
            String dir
    ) {
        return s3ImageUploadRequests.stream()
                .map(s3ImageUploadRequest -> {
                    var originalName = s3ImageUploadRequest.originalFilename();
                    var uniqueName = createUniqueName(
                            dir,
                            originalName
                    );

                    uploadImage(
                            uniqueName,
                            s3ImageUploadRequest
                    );

                    return new S3ImageUploadResponse(
                            originalName,
                            uniqueName,
                            getPath(uniqueName)
                    );
                }).toList();
    }

    private void uploadImage(
            String uniqueName,
            S3ImageUploadRequest s3ImageUploadRequest
    ) {
        var metadata = new ObjectMetadata();
        metadata.setContentType(s3ImageUploadRequest.contentType());
        metadata.setContentLength(s3ImageUploadRequest.contentSize());

        amazonS3.putObject(
                bucket,
                uniqueName,
                s3ImageUploadRequest.inputStream(),
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
