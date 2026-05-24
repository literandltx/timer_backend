package com.literandltx.notificationservice.service;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void processNotification(NotificationRequestedEvent event);
}
