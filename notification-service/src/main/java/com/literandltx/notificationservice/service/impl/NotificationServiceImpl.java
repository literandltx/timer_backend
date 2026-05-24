package com.literandltx.notificationservice.service.impl;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.event.NotificationType;
import com.literandltx.notificationservice.service.NotificationService;
import com.literandltx.notificationservice.service.NotificationStrategy;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Map<NotificationType, NotificationStrategy> strategyMap;

    public NotificationServiceImpl(List<NotificationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(NotificationStrategy::getNotificationType, Function.identity()));
    }

    @Override
    public void processNotification(NotificationRequestedEvent event) {
        NotificationStrategy strategy = strategyMap.get(event.getNotificationType());

        if (strategy == null) {
            log.warn("No strategy implemented for notification type: {}", event.getNotificationType());
            return;
        }

        strategy.process(event);
    }
}
