package com.grocify.backend.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderCreatedEvent event) {

        kafkaTemplate.send("order-created", event)
                .whenComplete((result, ex) -> {

                    if (ex == null) {
                        System.out.println(
                                "KAFKA MESSAGE SENT: " + event.getOrderId()
                        );
                    } else {
                        System.out.println(
                                "KAFKA SEND FAILED: " + ex.getMessage()
                        );
                    }
                });
    }
}