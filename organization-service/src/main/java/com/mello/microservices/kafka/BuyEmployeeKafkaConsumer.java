package com.mello.microservices.kafka;

import com.adyen.service.exception.ApiException;
import com.mello.basedomains.basedomains.dto.OrderEvent;
import com.mello.microservices.dto.OrganizationDto;
import com.mello.microservices.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BuyEmployeeKafkaConsumer
{
    private final OrganizationService organizationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyEmployeeKafkaConsumer.class);

    public BuyEmployeeKafkaConsumer(OrganizationService organizationService)
    {
        this.organizationService = organizationService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-buy.name}", groupId = "employee-group")
    public void consume(OrderEvent orderEvent) throws IOException, ApiException
    {
        organizationService.sendPayment(orderEvent);
        LOGGER.info(String.format("JSON Consumer order message received: %s - %s", orderEvent.getMessage(), orderEvent.getOrder().getName()));

    }
}
