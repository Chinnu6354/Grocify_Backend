package com.grocify.backend.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class OrderEventConsumer {

    private final ObjectMapper objectMapper;

    public OrderEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "grocify-order-group-local"
    )
    public void consume(String message) {

        try {
            OrderCreatedEvent event =
                    objectMapper.readValue(message, OrderCreatedEvent.class);

            System.out.println(
                    "KAFKA EVENT RECEIVED: Order ID = "
                            + event.getOrderId()
                            + ", User ID = "
                            + event.getUserId()
                            + ", Total = "
                            + event.getTotalAmount()
                            + ", Status = "
                            + event.getStatus()
            );

        } catch (Exception e) {
            System.out.println(
                    "KAFKA EVENT PARSE FAILED: " + e.getMessage()
            );
        }
    }
}