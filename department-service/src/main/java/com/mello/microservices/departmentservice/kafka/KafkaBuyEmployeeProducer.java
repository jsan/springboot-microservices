package com.mello.microservices.departmentservice.kafka;

import com.mello.basedomains.basedomains.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaBuyEmployeeProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaBuyEmployeeProducer.class);

    @Value("${spring.kafka.topic-buy.name}")
    private String topicBuy;

    private final KafkaTemplate <String, OrderEvent> kafkaTemplate;

    public KafkaBuyEmployeeProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(OrderEvent orderEvent)
    {
        LOGGER.info(String.format("Producer sending message: %s", orderEvent.toString()));
        Message<OrderEvent> message = MessageBuilder
                .withPayload(orderEvent)
                .setHeader(KafkaHeaders.TOPIC, topicBuy)
                .build();
        kafkaTemplate.send(message);
    }
}
