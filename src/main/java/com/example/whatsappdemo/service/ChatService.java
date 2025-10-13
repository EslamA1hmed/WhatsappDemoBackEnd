package com.example.whatsappdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.entity.Message;
import com.example.whatsappdemo.entity.MessageChat;
import com.example.whatsappdemo.entity.IncomingMessage;
import com.example.whatsappdemo.enums.MessageType;
import com.example.whatsappdemo.mapper.IncomingMessageMapper;
import com.example.whatsappdemo.mapper.MessageMapper;
import com.example.whatsappdemo.repo.IncomingMessageRepo;
import com.example.whatsappdemo.repo.MessageChatRepo;
import com.example.whatsappdemo.repo.MessageRepo;

@Service
public class ChatService {
    @Autowired
    private MessageChatRepo messageChatRepo;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private IncomingMessageMapper incomingMessageMapper;
    @Autowired
    private IncomingMessageRepo incomingMessageRepo;
    @Autowired
    private MessageRepo messageRepo;

    public Page<MessageResponseDTO> getMessages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MessageChat> messageChatPage = messageChatRepo.findAll(pageable);
        List<MessageResponseDTO> result = new ArrayList<>();

        for (MessageChat messageChat : messageChatPage.getContent()) {
            try {
                if (messageChat.getMessageType().equals(MessageType.INCOMING)) {
                    Optional<com.example.whatsappdemo.entity.IncomingMessage> incomingMessage = 
                        incomingMessageRepo.findByMessageId(messageChat.getMessageRefId());
                    if (incomingMessage.isPresent()) {
                        result.add(incomingMessageMapper.toDTO(incomingMessage.get()));
                    }
                } else {
                    Optional<Message> message = messageRepo.findByMessageId(messageChat.getMessageRefId());
                    if (message.isPresent()) {
                        result.add(messageMapper.fromEntity(message.get()));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing message with refId: " + 
                    messageChat.getMessageRefId() + ": " + e.getMessage());
            }
        }

        return new PageImpl<>(result, pageable, messageChatPage.getTotalElements());
    }

    public Page<MessageResponseDTO> getMessagesByContact(String phoneNumber, int page, int size) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<MessageChat> messageChatPage = messageChatRepo.findBySenderOrReceiver(phoneNumber, pageable);
        List<MessageResponseDTO> result = new ArrayList<>();

        for (MessageChat messageChat : messageChatPage.getContent()) {
            try {
                if (messageChat.getMessageType().equals(MessageType.INCOMING)) {
                    Optional<com.example.whatsappdemo.entity.IncomingMessage> incomingMessage = 
                        incomingMessageRepo.findByMessageId(messageChat.getMessageRefId());
                    if (incomingMessage.isPresent()) {
                        result.add(incomingMessageMapper.toDTO(incomingMessage.get()));
                    }
                } else {
                    Optional<Message> message = messageRepo.findByMessageId(messageChat.getMessageRefId());
                    if (message.isPresent()) {
                        result.add(messageMapper.fromEntity(message.get()));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing message with refId: " + 
                    messageChat.getMessageRefId() + ": " + e.getMessage());
            }
        }

        return new PageImpl<>(result, pageable, messageChatPage.getTotalElements());
    }

    public MessageResponseDTO getLastMessageForContact(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }

        Optional<MessageChat> messageChatOpt = messageChatRepo.findTopBySenderOrReceiverOrderByCreatedAtDesc(phoneNumber);
        if (messageChatOpt.isEmpty()) {
            return null;
        }

        MessageChat messageChat = messageChatOpt.get();
        try {
            if (messageChat.getMessageType().equals(MessageType.INCOMING)) {
                Optional<com.example.whatsappdemo.entity.IncomingMessage> incomingMessage = 
                    incomingMessageRepo.findByMessageId(messageChat.getMessageRefId());
                return incomingMessage.map(incomingMessageMapper::toDTO).orElse(null);
            } else {
                Optional<Message> message = messageRepo.findByMessageId(messageChat.getMessageRefId());
                return message.map(MessageMapper::fromEntity).orElse(null);
            }
        } catch (Exception e) {
            System.err.println("Error processing message with refId: " + 
                messageChat.getMessageRefId() + ": " + e.getMessage());
            return null;
        }
    }
}