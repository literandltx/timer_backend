package com.literandltx.notificationservice.service;

import jakarta.mail.MessagingException;

public interface MailSender {
    void sendPlainText(String to, String subject, String body);

    void sendHtml(String to, String subject, String htmlBody) throws MessagingException;
}
