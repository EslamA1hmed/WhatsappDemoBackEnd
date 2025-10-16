package com.example.whatsappdemo.controller;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.service.ChatService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<MessageResponseDTO>> getAllMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(chatService.getMessages());
    }

    @GetMapping("/contact/{phoneNumber}")
    public ResponseEntity<Page<MessageResponseDTO>> getMessagesByContact(
            @PathVariable String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            return ResponseEntity.ok(chatService.getMessagesByContact(phoneNumber, page, size));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/last/{phoneNumber}")
    public ResponseEntity<MessageResponseDTO> getLastMessageForContact(
            @PathVariable String phoneNumber
    ) {
        MessageResponseDTO message = chatService.getLastMessageForContact(phoneNumber);
        return message != null ? ResponseEntity.ok(message) : ResponseEntity.noContent().build();
    }
}