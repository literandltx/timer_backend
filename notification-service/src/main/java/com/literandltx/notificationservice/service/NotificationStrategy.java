package com.literandltx.notificationservice.service;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.event.NotificationType;

public interface NotificationStrategy {
    void process(NotificationRequestedEvent event);

    NotificationType getNotificationType();
}
