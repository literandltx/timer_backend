package com.literandltx.reportservice.service;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.event.ReportStatus;
import com.literandltx.reportservice.event.ReportStatusEvent;
import com.literandltx.reportservice.producer.ReportStatusProducer;
import com.literandltx.reportservice.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportGeneratorService reportGeneratorService;
    private final StorageService storageService;
    private final ReportStatusProducer reportStatusProducer;

    public void processReport(ReportRequestedEvent event) {
        try {
            String storagePath = generateStoragePath(event);

            byte[] reportBytes = reportGeneratorService.generateReport(event);
            storageService.uploadFile(storagePath, reportBytes);

            ReportStatusEvent successEvent = new ReportStatusEvent(
                    event.getReportId(),
                    ReportStatus.COMPLETED,
                    storagePath,
                    ""
            );

            reportStatusProducer.sendStatusUpdate(event.getUserId(), successEvent);
            log.info("Successfully compiled and uploaded {} file with ID: {}", event.getReportType(), event.getReportId());
        } catch (Exception ex) {
            log.error("Failed handling report compilation job for ID: {}", event.getReportId(), ex);

            ReportStatusEvent failureEvent = new ReportStatusEvent(
                    event.getReportId(),
                    ReportStatus.FAILED,
                    null,
                    ex.getMessage()
            );
            reportStatusProducer.sendStatusUpdate(event.getUserId(), failureEvent);
        }
    }

    private String generateStoragePath(ReportRequestedEvent event) {
        return String.format("reports/%d/%s.%s",
                event.getUserId(),
                event.getReportId(),
                event.getReportType().name().toLowerCase());
    }
}
