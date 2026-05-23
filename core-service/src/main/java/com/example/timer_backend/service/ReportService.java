package com.example.timer_backend.service;

import com.example.timer_backend.dto.report.ReportRequestDto;
import com.example.timer_backend.dto.report.ReportStatusResponseDto;
import com.example.timer_backend.model.Report;
import com.example.timer_backend.model.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {
    ReportStatusResponseDto requestReport(User user, ReportRequestDto reportRequestDto);

    Report getReportByIdAndUser(UUID id, Long userId);
}
