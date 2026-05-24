package com.literandltx.notificationservice.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.literandltx.notificationservice.event.NotificationRequestedEvent;
import com.literandltx.notificationservice.event.NotificationType;
import com.literandltx.notificationservice.service.MailSender;
import com.literandltx.notificationservice.service.NotificationStrategy;
import com.literandltx.notificationservice.service.impl.strategy.PasswordResetNotificationStrategy;
import com.literandltx.notificationservice.service.impl.strategy.RegistrationNotificationStrategy;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NotificationStrategiesTest {

    @ParameterizedTest(name = "Testing {1} strategy")
    @MethodSource("provideStrategies")
    @DisplayName("Strategy should map to correct type and send expected email")
    void testStrategyProcessing(
            NotificationStrategy strategy,
            NotificationType expectedType,
            NotificationRequestedEvent event,
            String expectedSubject,
            String expectedHtml,
            MailSender mockMailSender
    ) throws Exception {
        // Assert
        assertThat(strategy.getNotificationType())
                .as("Strategy should advertise %s notification type", expectedType)
                .isEqualTo(expectedType);

        // Act
        strategy.process(event);

        // Assert
        verify(mockMailSender).sendHtml(eq(event.getEmail()), eq(expectedSubject), eq(expectedHtml));
    }

    private static Stream<Arguments> provideStrategies() {
        MailSender mockMailSender = mock(MailSender.class);
        String resetToken = "abc-123";
        NotificationRequestedEvent resetEvent = new NotificationRequestedEvent(
                999L, "user@example.com", NotificationType.PASSWORD_RESET, Map.of("resetToken", resetToken)
        );
        String resetHtml = String.format(
                "<p>Click <a href='http://localhost:8080/api/v1/reset?token=%s'>here</a> to reset.</p>", resetToken
        );
        NotificationRequestedEvent registrationEvent = new NotificationRequestedEvent(
                999L, "newuser@example.com", NotificationType.REGISTRATION, Map.of()
        );
        String registrationHtml = "<h1>Welcome!</h1><p>Thanks for signing up. We’re glad you’re here.</p>";

        return Stream.of(
                Arguments.of(
                        new PasswordResetNotificationStrategy(mockMailSender),
                        NotificationType.PASSWORD_RESET,
                        resetEvent,
                        "Password Reset Request",
                        resetHtml,
                        mockMailSender
                ),
                Arguments.of(
                        new RegistrationNotificationStrategy(mockMailSender),
                        NotificationType.REGISTRATION,
                        registrationEvent,
                        "Welcome to Our Service",
                        registrationHtml,
                        mockMailSender
                )
        );
    }
}
