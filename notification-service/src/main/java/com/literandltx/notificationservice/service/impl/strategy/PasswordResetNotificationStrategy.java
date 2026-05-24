package com.literandltx.notificationservice.service.impl.strategy;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.event.NotificationType;
import com.literandltx.notificationservice.service.MailSender;
import com.literandltx.notificationservice.service.NotificationStrategy;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetNotificationStrategy implements NotificationStrategy {

    private final MailSender mailSender;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.PASSWORD_RESET;
    }

    @Override
    public void process(NotificationRequestedEvent event) {
        Map<String, String> attributes = event.getAttributes();
        String resetToken = attributes.getOrDefault("token", "");

        if (resetToken.isBlank()) {
            log.error("Cannot send password reset to {} - missing resetToken", event.getEmail());
            return;
        }

        String frontendUrl = "http://localhost:3000/reset-password?token=" + resetToken;
        String html = String.format(
                "<p>Click <a href='%s'>here</a> to reset your password.</p>",
                frontendUrl
        );

        try {
            mailSender.sendHtml(event.getEmail(), "Password Reset Request", html);
        } catch (Exception e) {
            log.error("Failed to send reset email to {}", event.getEmail(), e);
        }
    }
}
