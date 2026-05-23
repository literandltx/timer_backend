package com.literandltx.reportservice.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.event.ReportStatus;
import com.literandltx.reportservice.event.ReportStatusEvent;
import com.literandltx.reportservice.event.ReportType;
import com.literandltx.reportservice.producer.ReportStatusProducer;
import com.literandltx.reportservice.service.ReportGeneratorService;
import com.literandltx.reportservice.service.storage.S3UploadService;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

public class ReportListenerIT extends BaseIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoSpyBean
    private S3UploadService s3UploadService;

    @MockitoSpyBean
    private ReportStatusProducer reportStatusProducer;

    @MockitoSpyBean
    private ReportGeneratorService reportGeneratorService;

    @Test
    void shouldProcessReportRequestAndProduceSuccessStatus() {
        UUID reportId = UUID.randomUUID();
        Long userId = 123L;
        
        ReportRequestedEvent requestEvent = new ReportRequestedEvent(
                reportId,
                userId,
                "test@literandltx.com",
                ReportType.TXT,
                Map.of()
        );

        String expectedS3Key = "reports/" + userId + "/" + reportId + ".txt";

        kafkaTemplate.send("report-topic", requestEvent);

        verify(s3UploadService, timeout(5000).times(1))
                .uploadFile(eq(expectedS3Key), any(byte[].class));

        ArgumentCaptor<ReportStatusEvent> eventCaptor = ArgumentCaptor.forClass(ReportStatusEvent.class);
        verify(reportStatusProducer, timeout(5000).times(1))
                .sendStatusUpdate(eq(userId), eventCaptor.capture());

        ReportStatusEvent producedEvent = eventCaptor.getValue();
        assertThat(producedEvent.getReportId()).isEqualTo(reportId);
        assertThat(producedEvent.getReportStatus()).isEqualTo(ReportStatus.COMPLETED);
        assertThat(producedEvent.getS3Key()).isEqualTo(expectedS3Key);
        assertThat(producedEvent.getErrorMessage()).isEmpty();
    }
}
