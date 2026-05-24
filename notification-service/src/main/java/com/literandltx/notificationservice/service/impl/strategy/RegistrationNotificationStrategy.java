package com.literandltx.notificationservice.service.impl.strategy;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.event.NotificationType;
import com.literandltx.notificationservice.service.MailSender;
import com.literandltx.notificationservice.service.NotificationStrategy;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationNotificationStrategy implements NotificationStrategy {

    private final MailSender mailSender;

    @Override
    public void process(NotificationRequestedEvent event) {
        String html = "<h1>Welcome!</h1><p>Thanks for signing up. We’re glad you’re here.</p>";
        try {
            mailSender.sendHtml(event.getEmail(), "Welcome to Our Service", html);
        } catch (MessagingException e) {
            log.error("Failed to send REGISTRATION email to {}", event.getEmail(), e);
        }
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.REGISTRATION;
    }

}
