package com.example.timer_backend.listener;

import com.example.timer_backend.event.ReportStatusEvent;
import com.example.timer_backend.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportRequestedEventListener {

    private final ReportRepository reportRepository;

    @KafkaListener(topics = "${app.kafka.topics.core}", groupId = "core-service-group")
    @Transactional
    public void onReportStatusUpdated(ReportStatusEvent event) {
        log.info("Received status update for Report ID: {}. Status: {}", event.getReportId(), event.getReportStatus());

        reportRepository.findById(event.getReportId()).ifPresentOrElse(report -> {
            report.setStatus(event.getReportStatus());
            report.setS3Key(event.getS3Key());
            report.setErrorMessage(event.getErrorMessage());

            reportRepository.save(report);
            log.info("Successfully updated Report ID: {} in database.", report.getId());
        }, () -> log.error("Report ID: {} not found in database!", event.getReportId()));
    }
}
