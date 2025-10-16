package com.example.whatsappdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageResponse;
import com.example.whatsappdemo.service.MessageService;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<WhatsAppMessageResponse> sendMessage(@RequestBody WhatsAppMessageDTO whatsAppMessageDTO) {
        return ResponseEntity.ok().body(messageService.sendMessage(whatsAppMessageDTO));
    }

    @GetMapping
    public ResponseEntity<Page<MessageResponseDTO>> findAllSentMessages(int page, int size) {
        return ResponseEntity.ok(messageService.findAllSentMessages(page, size));
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<MessagesStatisticsDTO>> findMessagesStatistics() {
        return ResponseEntity.ok(messageService.findMessagesStatistics());
    }

}
