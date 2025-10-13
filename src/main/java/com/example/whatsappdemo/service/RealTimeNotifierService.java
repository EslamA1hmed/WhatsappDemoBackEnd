package com.example.whatsappdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.dto.StatusDTO;

@Service
public class RealTimeNotifierService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private String getChatTopic(String phoneNumber) {
        return "/topic/chat/" + phoneNumber;
    }

    private String getStatusTopic(String phoneNumber) {
        return "/topic/status/" + phoneNumber;
    }

    public void notifyNewMessage(MessageResponseDTO messageDto, String phoneNumber) {
        messagingTemplate.convertAndSend(getChatTopic(phoneNumber), messageDto);
    }

    public void notifyMessageStatus(StatusDTO statusDto, String phoneNumber) {
        messagingTemplate.convertAndSend(getStatusTopic(phoneNumber), statusDto);
    }
}

