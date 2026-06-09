package com.literandltx.timer_backend.service;

import com.literandltx.timer_backend.dto.report.ReportFileDto;
import com.literandltx.timer_backend.dto.report.ReportRequestDto;
import com.literandltx.timer_backend.dto.report.ReportStatusResponseDto;
import com.literandltx.timer_backend.model.Report;
import com.literandltx.timer_backend.model.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    ReportStatusResponseDto requestReport(User user, ReportRequestDto reportRequestDto);

    Report getReportByIdAndUser(UUID id, Long userId);

    ReportFileDto downloadReport(UUID id, Long userId);

    ReportStatusResponseDto getReportStatusDto(UUID id, Long userId);
}
