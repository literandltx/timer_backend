package com.literandltx.notificationservice.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.event.NotificationType;
import com.literandltx.notificationservice.service.MailSender;
import com.literandltx.notificationservice.service.impl.strategy.PasswordResetNotificationStrategy;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PasswordResetNotificationStrategyTest {

    @Mock
    private MailSender mailSender;

    private PasswordResetNotificationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PasswordResetNotificationStrategy(mailSender);
    }

    @Test
    @DisplayName("Should advertise PASSWORD_RESET notification type")
    void shouldReturnCorrectNotificationType() {
        assertThat(strategy.getNotificationType())
                .isEqualTo(NotificationType.PASSWORD_RESET);
    }

    @Test
    @DisplayName("Should send HTML email when reset token is provided")
    void shouldSendEmailSuccessfully() throws Exception {
        // Arrange
        String email = "user@example.com";
        String token = "abc-123";
        NotificationRequestedEvent event = createEvent(email, Map.of("token", token));

        String expectedHtml = String.format(
                "<p>Click <a href='http://localhost:3000/reset-password?token=%s'>here</a> to reset your password.</p>",
                token
        );

        // Act
        strategy.process(event);

        // Assert
        verify(mailSender).sendHtml(eq(email), eq("Password Reset Request"), eq(expectedHtml));
    }

    @Test
    @DisplayName("Should abort processing and not send email if reset token is missing")
    void shouldNotSendEmailWhenTokenIsMissing() {
        // Arrange
        NotificationRequestedEvent event = createEvent("user@example.com", Map.of()); // Missing token

        // Act
        strategy.process(event);

        // Assert
        verifyNoInteractions(mailSender);
    }

    @Test
    @DisplayName("Should handle MailSender exceptions gracefully without throwing")
    void shouldHandleMailSenderExceptions() throws Exception {
        // Arrange
        String email = "user@example.com";
        NotificationRequestedEvent event = createEvent(email, Map.of("token", "token"));

        doThrow(new RuntimeException("Mail server down"))
                .when(mailSender).sendHtml(eq(email), eq("Password Reset Request"), org.mockito.ArgumentMatchers.anyString());

        // Act & Assert
        assertDoesNotThrow(() -> strategy.process(event),
                "Exceptions from MailSender should be caught and logged");
    }

    private NotificationRequestedEvent createEvent(String email, Map<String, String> attributes) {
        return new NotificationRequestedEvent(
                999L,
                email,
                NotificationType.PASSWORD_RESET,
                attributes
        );
    }
}
