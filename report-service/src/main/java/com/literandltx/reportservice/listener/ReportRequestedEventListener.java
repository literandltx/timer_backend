package com.literandltx.reportservice.listener;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportRequestedEventListener {
    private final ReportService reportService;

    @KafkaListener(topics = "${app.kafka.topics.report}")
    public void onReportRequested(ReportRequestedEvent event) {
        log.info("Received execution job for report ID: {}", event.getReportId());
        log.debug("Execution job payload: {}", event);

        reportService.processReport(event);
    }
}
