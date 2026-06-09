package com.literandltx.timer_backend.controller;

import com.literandltx.timer_backend.dto.report.ReportFileDto;
import com.literandltx.timer_backend.dto.report.ReportRequestDto;
import com.literandltx.timer_backend.dto.report.ReportStatusResponseDto;
import com.literandltx.timer_backend.model.User;
import com.literandltx.timer_backend.service.ReportService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
        ReportStatusResponseDto response = reportService.requestReport(user, reportRequestDto);
        return ResponseEntity.accepted()
                .body(response);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ReportStatusResponseDto> getReportStatus(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        ReportStatusResponseDto response = reportService.getReportStatusDto(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadReport(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        ReportFileDto response = reportService.downloadReport(id, user.getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(response.getContentType()))
                .body(response.getResource());
    }
}
