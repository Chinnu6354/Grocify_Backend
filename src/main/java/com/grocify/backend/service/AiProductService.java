package com.grocify.backend.service;

import com.grocify.backend.entity.Product;
import com.grocify.backend.repository.ProductRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiProductService {

    private final ProductRepository productRepository;
    private final ChatClient chatClient;

    public AiProductService(
            ProductRepository productRepository,
            ChatClient.Builder builder
    ) {
        this.productRepository = productRepository;
        this.chatClient = builder.build();
    }

    public String ask(String message) {

        List<Product> products = productRepository.findAll();

        StringBuilder catalog = new StringBuilder();

        for (Product product : products) {
            catalog.append("Name: ").append(product.getName())
                    .append(", Category: ").append(product.getCategory())
                    .append(", Price: ₹").append(product.getPrice())
                    .append(", Description: ").append(product.getDescription())
                    .append("\n");
        }

        String prompt = """
                You are Grocify's smart grocery assistant.

                Recommend products ONLY from the product catalog below.
                Do not invent products.
                Mention product name and price.
                Keep the answer short and useful.

                Product Catalog:
                %s

                Customer Request:
                %s
                """.formatted(catalog, message);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}