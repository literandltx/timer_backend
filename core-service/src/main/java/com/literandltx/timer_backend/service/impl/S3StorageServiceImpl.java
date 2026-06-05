package com.literandltx.timer_backend.service.impl;

import com.literandltx.timer_backend.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements StorageService {

    private final S3Client s3Client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @Override
    public Resource downloadFile(String fileKey) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            return new InputStreamResource(s3Object) {
                @Override
                public String getFilename() {
                    return fileKey.contains("/")
                            ? fileKey.substring(fileKey.lastIndexOf("/") + 1)
                            : fileKey;
                }

                @Override
                public long contentLength() {
                    return s3Object.response().contentLength();
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Error reading file from storage: " + e.getMessage(), e);
        }
    }
}
