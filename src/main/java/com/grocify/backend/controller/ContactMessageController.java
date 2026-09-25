package com.grocify.backend.controller;

import com.grocify.backend.entity.ContactMessage;
import com.grocify.backend.service.ContactMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageController(
            ContactMessageService contactMessageService
    ) {
        this.contactMessageService = contactMessageService;
    }

    // =========================
    // USER SEND MESSAGE
    // =========================

    @PostMapping
    public ResponseEntity<ContactMessage> sendMessage(
            @RequestBody ContactMessage contactMessage
    ) {

        ContactMessage savedMessage =
                contactMessageService.saveMessage(contactMessage);

        return ResponseEntity.ok(savedMessage);
    }

    // =========================
    // ADMIN GET ALL MESSAGES
    // =========================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessage>> getAllMessages() {

        return ResponseEntity.ok(
                contactMessageService.getAllMessages()
        );
    }

    // =========================
    // ADMIN DELETE MESSAGE
    // =========================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteMessage(
            @PathVariable Long id
    ) {

        contactMessageService.deleteMessage(id);

        return ResponseEntity.ok(
                "Message deleted successfully"
        );
    }
}