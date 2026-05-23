package com.literandltx.reportservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
public class S3UploadService {

    private final S3Client s3Client;
    private final String bucketName;

    public S3UploadService(S3Client s3Client, @Value("${app.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void uploadFile(String s3Key, byte[] fileBytes) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));
            log.info("Successfully uploaded file to S3: {}/{}", bucketName, s3Key);

        } catch (Exception e) {
            log.error("Failed to upload file to S3", e);
            throw new RuntimeException("S3 Storage failure", e);
        }
    }
}
