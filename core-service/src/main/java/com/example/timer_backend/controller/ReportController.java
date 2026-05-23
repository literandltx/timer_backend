package com.example.timer_backend.controller;

import com.example.timer_backend.dto.report.ReportRequestDto;
import com.example.timer_backend.dto.report.ReportStatusResponseDto;
import com.example.timer_backend.model.Report;
import com.example.timer_backend.model.ReportStatus;
import com.example.timer_backend.model.User;
import com.example.timer_backend.service.ReportService;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

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
            Path filePath = Paths.get("./storage/", report.getS3Key()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file locally!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file path: " + e.getMessage());
        }
    }
}
