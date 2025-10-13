package com.example.whatsappdemo.service;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.entity.AllMessagesView;
import com.example.whatsappdemo.mapper.MessageViewMapper;
import com.example.whatsappdemo.repo.AllMessagesViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class MessageHistoryService {

    @Autowired
    private AllMessagesViewRepository repository;

    public Page<MessageResponseDTO> getMessages(int page, int size) {
        Page<AllMessagesView> data = repository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"))
        );

        var mapped = MessageViewMapper.mapList(data.getContent());
        return new PageImpl<>(mapped, data.getPageable(), data.getTotalElements());
    }
}
