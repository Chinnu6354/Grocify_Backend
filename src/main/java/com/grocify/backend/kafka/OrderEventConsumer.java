package com.grocify.backend.kafka;

import com.grocify.backend.service.OrderEmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class OrderEventConsumer {

    private final ObjectMapper objectMapper;
    private final OrderEmailService orderEmailService;

    public OrderEventConsumer(
            ObjectMapper objectMapper,
            OrderEmailService orderEmailService
    ) {
        this.objectMapper = objectMapper;
        this.orderEmailService = orderEmailService;
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
            System.out.println(
                    "KAFKA EMAIL VALUE: " + event.getUserEmail()
            );

            orderEmailService.sendOrderConfirmation(
                    event.getUserEmail(),
                    event.getOrderId(),
                    event.getTotalAmount()
            );

        } catch (Exception e) {
            System.out.println(
                    "KAFKA EVENT PROCESS FAILED: " + e.getMessage()
            );
        }
    }
}