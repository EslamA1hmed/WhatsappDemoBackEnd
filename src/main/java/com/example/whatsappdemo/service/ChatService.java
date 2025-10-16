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
import com.example.whatsappdemo.entity.AllMessages;
import com.example.whatsappdemo.entity.IncomingMessage;
import com.example.whatsappdemo.enums.MessageType;
import com.example.whatsappdemo.mapper.IncomingMessageMapper;
import com.example.whatsappdemo.mapper.MessageMapper;
import com.example.whatsappdemo.repo.IncomingMessageRepo;
import com.example.whatsappdemo.repo.AllMessagesRepo;
import com.example.whatsappdemo.repo.MessageRepo;

@Service
public class ChatService {
    @Autowired
    private AllMessagesRepo messageChatRepo;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private IncomingMessageMapper incomingMessageMapper;
    @Autowired
    private IncomingMessageRepo incomingMessageRepo;
    @Autowired
    private MessageRepo messageRepo;

    public List<MessageResponseDTO> getMessages() {
        List<AllMessages> messageChatPage = messageChatRepo.findAll();
        List<MessageResponseDTO> result = new ArrayList<>();

        for (AllMessages messageChat : messageChatPage) {
            try {
                if (messageChat.getMessageType().equals(MessageType.INCOMING)) {
                    Optional<com.example.whatsappdemo.entity.IncomingMessage> incomingMessage = incomingMessageRepo
                            .findByMessageId(messageChat.getMessageRefId());
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

        return result;
    }

    public Page<MessageResponseDTO> getMessagesByContact(String phoneNumber, int page, int size) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<AllMessages> messageChatPage = messageChatRepo.findBySenderOrReceiver(phoneNumber, pageable);
        List<MessageResponseDTO> result = new ArrayList<>();

        for (AllMessages messageChat : messageChatPage.getContent()) {
            try {
                if (messageChat.getMessageType().equals(MessageType.INCOMING)) {
                    Optional<com.example.whatsappdemo.entity.IncomingMessage> incomingMessage = incomingMessageRepo
                            .findByMessageId(messageChat.getMessageRefId());
                    incomingMessage.ifPresent(message -> result.add(incomingMessageMapper.toDTO(message)));
                } else {
                    Optional<Message> message = messageRepo.findByMessageId(messageChat.getMessageRefId());
                    message.ifPresent(m -> result.add(messageMapper.fromEntity(m)));
                }
            } catch (Exception e) {
                System.err.println("Error processing message with refId: " +
                        messageChat.getMessageRefId() + ": " + e.getMessage());
            }
        }

        // 🟩 هنا نضيف السطر اللي يطبع اللي رايح للفرونت إند
        System.out.println("📤 Returning messages to frontend:");
        for (MessageResponseDTO dto : result) {
            System.out.println("  -> ID: " + dto.getId() +
                    ", From: " + dto.getFrom() +
                    ", To: " + dto.getTo() +
                    ", Text: " + dto.getTextBody() +
                    ", Type: " + dto.getType());
        }

        // لو عايز كمان تشوف عدد العناصر والصفحات:
        System.out.println("Total messages in page: " + result.size());
        System.out.println("Total elements: " + messageChatPage.getTotalElements());
        System.out.println("Current page: " + page + ", Size: " + size);

        return new PageImpl<>(result, pageable, messageChatPage.getTotalElements());
    }

    public MessageResponseDTO getLastMessageForContact(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return null;
        }

        Optional<AllMessages> messageChatOpt = messageChatRepo
                .findTopBySenderOrReceiverOrderByCreatedAtDesc(phoneNumber);
        if (messageChatOpt.isEmpty()) {
            return null;
        }

        AllMessages messageChat = messageChatOpt.get();
        try {
            if (messageChat.getMessageType().equals(MessageType.INCOMING)) {
                Optional<com.example.whatsappdemo.entity.IncomingMessage> incomingMessage = incomingMessageRepo
                        .findByMessageId(messageChat.getMessageRefId());
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