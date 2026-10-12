package com.grocify.backend.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "grocify-order-group"
    )
    public void consume(OrderCreatedEvent event) {

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
    }
}