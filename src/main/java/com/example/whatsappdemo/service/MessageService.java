package com.example.whatsappdemo.service;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageResponse;
import com.example.whatsappdemo.entity.Contact;
import com.example.whatsappdemo.entity.Message;
import com.example.whatsappdemo.entity.AllMessages;
import com.example.whatsappdemo.enums.MessageType;
import com.example.whatsappdemo.mapper.MessageMapper;
import com.example.whatsappdemo.repo.ContactRepo;
import com.example.whatsappdemo.repo.AllMessagesRepo;
// import com.example.whatsappdemo.mapper.MessageMapper;
import com.example.whatsappdemo.repo.MessageRepo;
import com.example.whatsappdemo.repo.TemplateRepo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {
    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Autowired
    private AllMessagesRepo messageChatRepo;
    @Autowired 
    private RealTimeNotifierService realTimeNotifierService;

    // @Autowired
    // private MessageMapper messageMapper;

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private TemplateRepo templateRepo;
    @Autowired
    private MessageMapper messageMapper;
     @Autowired
    private ContactRepo contactRepo;
    private static final Logger logger = LoggerFactory.getLogger(IncomingMessageService.class);

    @Autowired
    private MediaService mediaService;

    private final RestTemplate restTemplate = new RestTemplate();

    public WhatsAppMessageResponse sendMessage(WhatsAppMessageDTO dto) {
        String url = apiUrl + "/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);
        if (dto.getTemplate() != null) {
            if (!templateRepo.findByName(dto.getTemplate().getName()).getStatus().equals("APPROVED")) {
                return null;
            }
        }
        HttpEntity<WhatsAppMessageDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<WhatsAppMessageResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                WhatsAppMessageResponse.class);
                Optional<Contact> contact = contactRepo.findByPhoneNumber(dto.getTo());
            if (contact.isPresent()) {
                contact.get().setLastMessage(LocalDateTime.now());
                contactRepo.save(contact.get());
            } else {
                contactRepo.save(Contact.builder().name(dto.getTo())
                        .phoneNumber(dto.getTo()).lastMessage(LocalDateTime.now()).build());
            }
        Message message = messageMapper.toEntity(dto);        
        message.setMessageId(response.getBody().getMessages().get(0).getId());
        
        if ("image".equals(message.getType()) && message.getMediaId() != null) {
                try {
                    byte[] imageData = mediaService.downloadMedia(message.getMediaId());
                    Map<String, Object> imageDetails = mediaService.processImage(imageData);
                    
                    message.setWidth((Integer) imageDetails.get("width"));
                    message.setHeight((Integer) imageDetails.get("height"));
                    message.setThumbnail((String) imageDetails.get("thumbnail"));

                } catch (IOException e) {
                    logger.error("Failed to process sending image with mediaId: {}", message.getMediaId(), e);
                    // يمكنك ترك الحقول فارغة والمتابعة
                }
            }
        messageRepo.save(message);
        messageChatRepo.save(AllMessages.builder().messageRefId(message.getMessageId()).messageType(MessageType.OUTGOING).
        receiver(message.getTo()).sender(message.getFrom()).build());
        realTimeNotifierService.notifyNewMessage(messageMapper.fromEntity(message), dto.getTo());
        return response.getBody();
    }

    public Page<MessageResponseDTO> findAllSentMessages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagesPage = messageRepo.findAllByOrderByCreatedAtDesc(pageable);
        return messagesPage.map(MessageMapper::fromEntity);
    }

    public List<MessagesStatisticsDTO> findMessagesStatistics() {
        return messageRepo.getMessagesStatistics();
    }

}
