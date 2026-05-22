package com.literandltx.notificationservice.it;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.listener.NotificationRequestedEventListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

public class NotificationListenerIT extends BaseIntegrationTest {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @MockitoSpyBean
    private NotificationRequestedEventListener listener;

    @Test
    void shouldConsumeNotificationEvent() {
        NotificationRequestedEvent event = new NotificationRequestedEvent(
                123L,
                "test@literandltx.com",
                "EMAIL_VERIFICATION"
        );

        kafkaTemplate.send("notification-topic", event);

        verify(listener, timeout(5000).times(1)).onReportRequested(event);
    }
}
