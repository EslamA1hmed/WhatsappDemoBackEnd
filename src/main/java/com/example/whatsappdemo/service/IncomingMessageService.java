package com.example.whatsappdemo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.whatsappdemo.dto.WhatsAppWebhookDTO;
import com.example.whatsappdemo.entity.Contact;
import com.example.whatsappdemo.entity.IncomingMessage;
import com.example.whatsappdemo.entity.AllMessages;
import com.example.whatsappdemo.enums.MessageType;
import com.example.whatsappdemo.mapper.IncomingMessageMapper;
import com.example.whatsappdemo.repo.ContactRepo;
import com.example.whatsappdemo.repo.IncomingMessageRepo;
import com.example.whatsappdemo.repo.AllMessagesRepo;

import jakarta.transaction.Transactional;

@Service
public class IncomingMessageService {
    private static final Logger logger = LoggerFactory.getLogger(IncomingMessageService.class);

    @Autowired
    private IncomingMessageRepo incomingMessageRepo;
     @Autowired 
    private RealTimeNotifierService realTimeNotifierService;

    @Autowired
    private IncomingMessageMapper incomingMessageMapper;
    @Autowired
    private AllMessagesRepo messageChatRepo;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private MediaService mediaService;

    public String saveMessage(WhatsAppWebhookDTO whatsAppWebhookDTO) {
        List<IncomingMessage> incomingMessages = incomingMessageMapper.toEntities(whatsAppWebhookDTO);
        List<AllMessages> messageChat = new ArrayList<>();
        for (IncomingMessage incomingMessage : incomingMessages) {
            messageChat.add(
                    AllMessages.builder().messageRefId(incomingMessage.getMessageId()).messageType(MessageType.INCOMING)
                            .receiver(incomingMessage.getTo()).sender(incomingMessage.getFrom()).build());
            Optional<Contact> contact = contactRepo.findByPhoneNumber(incomingMessage.getFrom());
            if ("image".equals(incomingMessage.getType()) && incomingMessage.getMediaId() != null) {
                try {
                    byte[] imageData = mediaService.downloadMedia(incomingMessage.getMediaId());
                    Map<String, Object> imageDetails = mediaService.processImage(imageData);
                    
                    incomingMessage.setWidth((Integer) imageDetails.get("width"));
                    incomingMessage.setHeight((Integer) imageDetails.get("height"));
                    incomingMessage.setThumbnail((String) imageDetails.get("thumbnail"));

                } catch (IOException e) {
                    logger.error("Failed to process incoming image with mediaId: {}", incomingMessage.getMediaId(), e);
                    // يمكنك ترك الحقول فارغة والمتابعة
                }
            }
            if (contact.isPresent()) {
                contact.get().setUnread(contact.get().getUnread()+1);
                contact.get().setLastMessage(LocalDateTime.now());
                contactRepo.save(contact.get());
            } else {
                contactRepo.save(Contact.builder().name(incomingMessage.getFrom())
                        .phoneNumber(incomingMessage.getFrom()).lastMessage(LocalDateTime.now()).build());
            }
            realTimeNotifierService.notifyNewMessage(incomingMessageMapper.toDTO(incomingMessage), incomingMessage.getFrom());
        }
        messageChatRepo.saveAll(messageChat);
        incomingMessageRepo.saveAll(incomingMessages);
        return "Saved successfully";
    }
    
    public String markMessagesAsRead(String phoneNumber){
        List<IncomingMessage> messages = incomingMessageRepo.findMessagesByStatus(phoneNumber);
        for (IncomingMessage incomingMessage : messages) {
            incomingMessage.setStatus("read");
        }
        incomingMessageRepo.saveAll(messages);
        Contact contact = contactRepo.findByPhoneNumber(phoneNumber).get();
        contact.setUnread(0L);
        contactRepo.save(contact);
        return "Done";
    }
}
