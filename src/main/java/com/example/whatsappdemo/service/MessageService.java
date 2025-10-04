package com.example.whatsappdemo.service;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageResponse;
import com.example.whatsappdemo.entity.Message;
import com.example.whatsappdemo.mapper.MessageMapper;
// import com.example.whatsappdemo.mapper.MessageMapper;
import com.example.whatsappdemo.repo.MessageRepo;
import com.example.whatsappdemo.repo.TemplateRepo;

import java.util.List;

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

    // @Autowired
    // private MessageMapper messageMapper;

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private TemplateRepo templateRepo;
    @Autowired
    private MessageMapper messageMapper;

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
        Message message = messageMapper.toEntity(dto);
        message.setMessageId(response.getBody().getMessages().get(0).getId());
        messageRepo.save(message);
        return response.getBody();
    }

    public Page<MessageResponseDTO> findAllSentMessages(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagesPage = messageRepo.findAllByOrderByCreatedAtDesc(pageable);
        return messagesPage.map(MessageMapper::fromEntity);
    }

    public List<MessagesStatisticsDTO> findMessagesStatistics() {
        return messageRepo.findStatisticsDTO();
    }
}
