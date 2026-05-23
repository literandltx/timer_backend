package com.literandltx.reportservice.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.event.ReportType;
import com.literandltx.reportservice.service.strategy.PdfReportGeneratorStrategy;
import com.literandltx.reportservice.service.strategy.TxtReportGeneratorStrategy;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportGeneratorStrategiesTest {

    @Test
    @DisplayName("PDF Strategy should generate non-empty byte array and map to PDF type")
    void testPdfStrategyGeneration() {
        // Arrange
        PdfReportGeneratorStrategy strategy = new PdfReportGeneratorStrategy();
        ReportRequestedEvent event = createEvent(ReportType.PDF);

        // Act
        byte[] result = assertDoesNotThrow(() -> strategy.generate(event), 
                "PDF generation should not throw any exceptions");

        // Assert
        assertThat(result)
                .as("Generated PDF byte array should not be null or empty")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(strategy.getReportType())
                .as("Strategy should advertise PDF report type")
                .isEqualTo(ReportType.PDF);
    }

    @Test
    @DisplayName("TXT Strategy should generate non-empty byte array and map to TXT type")
    void testTxtStrategyGeneration() {
        // Arrange
        TxtReportGeneratorStrategy strategy = new TxtReportGeneratorStrategy();
        ReportRequestedEvent event = createEvent(ReportType.TXT);

        // Act
        byte[] result = assertDoesNotThrow(() -> strategy.generate(event), 
                "TXT generation should not throw any exceptions");

        // Assert
        assertThat(result)
                .as("Generated TXT byte array should not be null or empty")
                .isNotNull()
                .isNotEmpty();

        assertThat(strategy.getReportType())
                .as("Strategy should advertise TXT report type")
                .isEqualTo(ReportType.TXT);
    }

    private ReportRequestedEvent createEvent(ReportType type) {
        return new ReportRequestedEvent(
                UUID.randomUUID(),
                100L,
                "user@example.com",
                type,
                Map.of("fromDate", "2026-05-01", "toDate", "2026-05-23")
        );
    }
}