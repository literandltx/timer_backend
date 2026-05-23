package com.example.timer_backend.service.impl;

import com.example.timer_backend.dto.report.ReportFileDto;
import com.example.timer_backend.dto.report.ReportRequestDto;
import com.example.timer_backend.dto.report.ReportStatusResponseDto;
import com.example.timer_backend.event.ReportRequestedEvent;
import com.example.timer_backend.model.Report;
import com.example.timer_backend.model.ReportStatus;
import com.example.timer_backend.model.User;
import com.example.timer_backend.producer.ReportEventPublisher;
import com.example.timer_backend.repository.ReportRepository;
import com.example.timer_backend.service.ReportService;
import com.example.timer_backend.service.StorageService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportEventPublisher eventPublisher;
    private final StorageService storageService;

    @Override
    @Transactional
    public ReportStatusResponseDto requestReport(User user, ReportRequestDto reportRequest) {
        Report report = new Report();
        report.setUserId(user.getId());
        report.setReportType(reportRequest.getReportType());
        report.setStatus(ReportStatus.PENDING);

        report = reportRepository.save(report);

        ReportRequestedEvent event = new ReportRequestedEvent();
        event.setReportId(report.getId());
        event.setUserId(user.getId());
        event.setEmail(user.getEmail());
        event.setReportType(reportRequest.getReportType());
        event.setFilters(reportRequest.getFilters());

        eventPublisher.publishReportRequest(event);

        return mapToStatusDto(report);
    }

    @Override
    public ReportStatusResponseDto getReportStatusDto(UUID id, Long userId) {
        Report report = getReportByIdAndUser(id, userId);
        return mapToStatusDto(report);
    }

    @Override
    public ReportFileDto downloadReport(UUID id, Long userId) {
        Report report = getReportByIdAndUser(id, userId);

        if (report.getStatus() != ReportStatus.COMPLETED) {
            throw new RuntimeException("Report is not ready yet.");
        }

        Resource resource = storageService.downloadFile(report.getS3Key());

        ReportFileDto response = new ReportFileDto();
        response.setResource(resource);
        response.setFilename(resource.getFilename());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return response;
    }

    @Override
    public Report getReportByIdAndUser(UUID id, Long userId) {
        return reportRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Report not found or permission denied."));
    }

    private ReportStatusResponseDto mapToStatusDto(Report report) {
        ReportStatusResponseDto response = new ReportStatusResponseDto();
        response.setId(report.getId());
        response.setStatus(report.getStatus().toString());
        return response;
    }
}
