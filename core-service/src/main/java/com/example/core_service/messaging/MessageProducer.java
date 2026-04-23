package com.example.core_service.messaging;

import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import com.example.core_service.config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final AmqpTemplate amqpTemplate;

    public void send(Map<String, Object> message) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
        log.info("Sent: {}", message);
    }
}
