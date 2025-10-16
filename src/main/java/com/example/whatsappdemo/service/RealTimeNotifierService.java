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

    // ✅ بعت notification للمستقبل
    public void notifyNewMessage(MessageResponseDTO messageDto, String recipientPhone) {
        messagingTemplate.convertAndSend(getChatTopic(recipientPhone), messageDto);
    }

    // ✅ NEW: بعت notification للمرسل كمان
    public void notifyMessageSent(MessageResponseDTO messageDto, String senderPhone) {
        messagingTemplate.convertAndSend(getChatTopic(senderPhone), messageDto);
    }

    public void notifyMessageStatus(StatusDTO statusDto, String phoneNumber) {
        messagingTemplate.convertAndSend(getStatusTopic(phoneNumber), statusDto);
    }
}

