package com.mello.microservices.employeeservice.kafka;

import com.mello.microservices.employeeservice.dto.EmployeeDto;
import com.mello.microservices.employeeservice.dto.EmployeeEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaEmployeeProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEmployeeProducer.class);
    private NewTopic topic;
    private KafkaTemplate <String, EmployeeEvent> kafkaTemplate;

    public KafkaEmployeeProducer(NewTopic topic, KafkaTemplate<String, EmployeeEvent> kafkaTemplate)
    {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(EmployeeEvent employeeEvent)
    {
        LOGGER.info(String.format("Producer sending message: %s", employeeEvent.toString()));
        Message<EmployeeEvent> message = MessageBuilder
                .withPayload(employeeEvent)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();
        kafkaTemplate.send(message);
    }
}
