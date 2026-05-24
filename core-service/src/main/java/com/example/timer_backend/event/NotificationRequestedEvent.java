package com.example.timer_backend.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestedEvent {
    private Long userId;
    private String email;
    private NotificationType notificationType;
    private Map<String, String> attributes;
}
