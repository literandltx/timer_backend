package com.example.timer_backend.controller;

import com.example.timer_backend.dto.report.ReportRequestDto;
import com.example.timer_backend.dto.report.ReportStatusResponseDto;
import com.example.timer_backend.model.Report;
import com.example.timer_backend.model.ReportStatus;
import com.example.timer_backend.model.User;
import com.example.timer_backend.service.ReportService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    private final S3Client s3Client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @PostMapping
    public ResponseEntity<ReportStatusResponseDto> requestReport(
            @AuthenticationPrincipal User user,
            @RequestBody ReportRequestDto reportRequestDto
    ) {
        ReportStatusResponseDto report = reportService.requestReport(user, reportRequestDto);
        return ResponseEntity.accepted().body(report);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ReportStatusResponseDto> getReportStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        Report report = reportService.getReportByIdAndUser(id, user.getId());

        ReportStatusResponseDto response = new ReportStatusResponseDto();
        response.setId(report.getId());
        response.setStatus(report.getStatus().toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadReport(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        Report report = reportService.getReportByIdAndUser(id, user.getId());

        if (report.getStatus() != ReportStatus.COMPLETED) {
            throw new RuntimeException("Report is not ready yet.");
        }

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(report.getS3Key())
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            InputStreamResource resource = new InputStreamResource(s3Object);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getS3Key() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Error reading file from S3: " + e.getMessage());
        }
    }
}
