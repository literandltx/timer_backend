package com.literandltx.notificationservice.listener;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequestedEventListener {

    private final NotificationServiceImpl notificationService;

    @KafkaListener(topics = "${app.kafka.topics.notification}")
    public void onReportRequested(NotificationRequestedEvent event) {
        log.info("Notification event received from Kafka: {}", event);

        notificationService.processNotification(event);
    }
}
