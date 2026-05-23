package com.literandltx.reportservice.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.event.ReportType;
import com.literandltx.reportservice.service.strategy.PdfReportGeneratorStrategy;
import com.literandltx.reportservice.service.strategy.ReportGeneratorStrategy;
import com.literandltx.reportservice.service.strategy.TxtReportGeneratorStrategy;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReportGeneratorStrategiesTest {

    @ParameterizedTest(name = "Testing {0} for type {1}")
    @MethodSource("provideStrategies")
    @DisplayName("Strategy should generate non-empty byte array and map to correct type")
    void testStrategyGeneration(ReportGeneratorStrategy strategy, ReportType expectedType) {
        // Arrange
        ReportRequestedEvent event = createEvent(expectedType);

        // Act
        byte[] result = assertDoesNotThrow(() -> strategy.generate(event), 
                expectedType + " generation should not throw any exceptions");

        // Assert
        assertThat(result)
                .as("Generated %s byte array should not be null or empty", expectedType)
                .isNotNull()
                .isNotEmpty();
        assertThat(strategy.getReportType())
                .as("Strategy should advertise %s report type", expectedType)
                .isEqualTo(expectedType);
    }

    private static Stream<Arguments> provideStrategies() {
        return Stream.of(
                Arguments.of(new PdfReportGeneratorStrategy(), ReportType.PDF),
                Arguments.of(new TxtReportGeneratorStrategy(), ReportType.TXT)
        );
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
