package com.sixheroes.onedayheroapi.global.s3;

import com.sixheroes.onedayheroapplication.global.s3.dto.request.S3ImageUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public final class MultipartFileMapper {

    public static List<S3ImageUploadRequest> mapToS3ImageUploadFileRequests(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(multipartFile -> {
                    try {
                        return mapToS3ImageUploadFileRequest(multipartFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    private static S3ImageUploadRequest mapToS3ImageUploadFileRequest(MultipartFile multipartFile) throws IOException {
        return new S3ImageUploadRequest(
                multipartFile.getInputStream(),
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize()
        );
    }
}
