package com.literandltx.reportservice.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import com.literandltx.reportservice.event.ReportRequestedEvent;
import com.literandltx.reportservice.event.ReportStatus;
import com.literandltx.reportservice.event.ReportStatusEvent;
import com.literandltx.reportservice.event.ReportType;
import com.literandltx.reportservice.service.ReportGeneratorService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Import(ReportListenerIT.LocalStackS3Config.class)
public class ReportListenerIT extends BaseIntegrationTest {

    private static final BlockingQueue<ReportStatusEvent> statusEventsQueue = new LinkedBlockingQueue<>();

    @TestConfiguration
    static class LocalStackS3Config {
        @Bean
        @Primary
        public S3Client testS3Client(
                @Value("${spring.cloud.aws.s3.endpoint}") String endpoint,
                @Value("${spring.cloud.aws.credentials.access-key}") String accessKey,
                @Value("${spring.cloud.aws.credentials.secret-key}") String secretKey,
                @Value("${spring.cloud.aws.region.static}") String region
        ) {
            return S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                    .forcePathStyle(true)
                    .build();
        }
    }

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private S3Client s3Client;

    @MockitoSpyBean
    private ReportGeneratorService reportGeneratorService;

    @Value("${app.kafka.topics.report}")
    private String reportTopic;

    @Value("${app.s3.bucket}")
    private String bucketName;

    @BeforeEach
    void clearQueue() {
        statusEventsQueue.clear();
    }

    @KafkaListener(
            topics = "${app.kafka.topics.core}",
            groupId = "it-assertion-group",
            properties = {
                    "auto.offset.reset=earliest",
                    "spring.json.trusted.packages=*"
            }
    )
    public void listenCoreTopic(ReportStatusEvent event) {
        statusEventsQueue.add(event);
    }

    @Test
    void shouldProcessReportSuccessfully_AndUploadToS3() throws Exception {
        // Arrange
        UUID reportId = UUID.randomUUID();
        Long userId = 123L;
        ReportType type = ReportType.values()[0];

        ReportRequestedEvent requestEvent = new ReportRequestedEvent(
                reportId, userId, "test@example.com", type,
                Map.of("fromDate", "2026-05-01", "toDate", "2026-05-23")
        );

        String expectedPath = String.format("reports/%d/%s.%s",
                userId, reportId, type.name().toLowerCase());

        byte[] reportContent = "pdf-content".getBytes(StandardCharsets.UTF_8);

        doReturn(reportContent)
                .when(reportGeneratorService).generateReport(any(ReportRequestedEvent.class));

        // Act
        kafkaTemplate.send(reportTopic, String.valueOf(userId), requestEvent).get();

        // Assert
        ReportStatusEvent statusEvent = statusEventsQueue.poll(10, TimeUnit.SECONDS);

        assertThat(statusEvent)
                .as("No event received from Kafka within 10 seconds")
                .isNotNull();
        assertThat(statusEvent.getReportStatus()).isEqualTo(ReportStatus.COMPLETED);
        assertThat(statusEvent.getReportId()).isEqualTo(reportId);
        assertThat(statusEvent.getS3Key()).isEqualTo(expectedPath);
        assertThat(statusEvent.getErrorMessage()).isEmpty();
        ResponseBytes<GetObjectResponse> s3Object = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(expectedPath)
                        .build()
        );
        assertThat(s3Object.asUtf8String()).isEqualTo("pdf-content");
    }

    @Test
    void shouldHandleReportGenerationFailure_AndPublishFailedEvent() throws Exception {
        // Arrange
        UUID reportId = UUID.randomUUID();
        Long userId = 456L;
        ReportType type = ReportType.values()[0];

        ReportRequestedEvent requestEvent = new ReportRequestedEvent(
                reportId, userId, "fail@example.com", type, Map.of()
        );

        doThrow(new RuntimeException("Simulated generation failure"))
                .when(reportGeneratorService).generateReport(any(ReportRequestedEvent.class));

        // Act
        kafkaTemplate.send(reportTopic, String.valueOf(userId), requestEvent).get();

        // Assert
        ReportStatusEvent statusEvent = statusEventsQueue.poll(10, TimeUnit.SECONDS);

        assertThat(statusEvent)
                .as("No event received from Kafka within 10 seconds")
                .isNotNull();

        assertThat(statusEvent.getReportStatus()).isEqualTo(ReportStatus.FAILED);
        assertThat(statusEvent.getReportId()).isEqualTo(reportId);
        assertThat(statusEvent.getS3Key()).isNull();
        assertThat(statusEvent.getErrorMessage()).isEqualTo("Simulated generation failure");
    }
}
