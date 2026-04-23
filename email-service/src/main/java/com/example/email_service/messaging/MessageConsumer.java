package com.example.email_service.messaging;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.email_service.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:}")
    private String mailFrom;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receive(Map<String, Object> message) {
        log.info("Received: {}", message);
        try {
            String mailTo = readField(message, "email");
            if (mailTo == null || mailTo.isBlank()) {
                log.warn("Skip email: missing 'email' field in message {}", message);
                return;
            }

            String username = readField(message, "username");
            String eventType = readField(message, "eventType");
            String orderId = readField(message, "orderId");
            String status = readField(message, "status");
            String updatedAt = readField(message, "updatedAt");

            String subject = "Order status changed";
            if (eventType != null && !eventType.isBlank()) {
                subject = "[" + eventType + "] Order update";
            }

            String body = "Hello " + (username == null || username.isBlank() ? "customer" : username) + ",\n\n"
                    + "Your order"
                    + (orderId == null || orderId.isBlank() ? "" : " #" + orderId)
                    + " is now in status: " + (status == null || status.isBlank() ? "UNKNOWN" : status) + ".\n"
                    + (updatedAt == null || updatedAt.isBlank() ? "" : "Updated at: " + updatedAt + "\n")
                    + "\nThank you.";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(mailTo);
            if (mailFrom != null && !mailFrom.isBlank()) {
                email.setFrom(mailFrom);
            }
            email.setSubject(subject);
            email.setText(body);

            mailSender.send(email);
            log.info("Email sent to {}", mailTo);
        } catch (MailException ex) {
            log.error("Failed to send email for message: {}", message, ex);
        } catch (RuntimeException ex) {
            log.error("Failed to send email for message: {}", message, ex);
        }
    }

    private String readField(Map<String, Object> message, String key) {
        Object value = message.get(key);
        return value == null ? null : String.valueOf(value);
    }
}
