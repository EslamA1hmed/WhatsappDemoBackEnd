package com.example.whatsappdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsappdemo.service.IncomingMessageService;

@RestController
@RequestMapping("incoming/messages")
public class IncomingMessagesController {

    @Autowired 
    private IncomingMessageService incomingMessageService;
     @PutMapping("/read/{phoneNumber}")
    public ResponseEntity<?> markMessagesAsRead(@PathVariable String phoneNumber) {
        incomingMessageService.markMessagesAsRead(phoneNumber);
        return ResponseEntity.ok().build();
    }
}
