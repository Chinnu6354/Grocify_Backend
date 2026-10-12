package com.grocify.backend.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(String message) {

        kafkaTemplate.send("order-created", message)
                .whenComplete((result, ex) -> {

                    if (ex == null) {
                        System.out.println(
                                "KAFKA MESSAGE SENT: " + message
                        );
                    } else {
                        System.out.println(
                                "KAFKA SEND FAILED: " + ex.getMessage()
                        );
                    }
                });
    }
}