package com.grocify.backend.controller;

import com.grocify.backend.service.AiProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final AiProductService aiProductService;

    public AiController(AiProductService aiProductService) {
        this.aiProductService = aiProductService;
    }

    @GetMapping("/api/ai")
    public String askAi(@RequestParam String message) {
        return aiProductService.ask(message);
    }
}