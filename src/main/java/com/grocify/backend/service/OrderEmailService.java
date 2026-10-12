package com.grocify.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderEmailService {

    private final JavaMailSender mailSender;

    public OrderEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(
            String email,
            Long orderId,
            Double totalAmount
    ) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Grocify Order Confirmation");

        message.setText(
                "Your order has been placed successfully.\n\n"
                        + "Order ID: " + orderId
                        + "\nTotal Amount: ₹" + totalAmount
                        + "\nStatus: PLACED"
        );

        mailSender.send(message);

        System.out.println(
                "ORDER CONFIRMATION EMAIL SENT: " + email
        );
    }
}