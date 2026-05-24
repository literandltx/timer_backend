package com.literandltx.notificationservice.event;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestedEvent {
    private Long userId;
    private String email;
    private NotificationType notificationType;
    private Map<String, String> attributes;
}
