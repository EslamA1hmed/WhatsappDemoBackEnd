package com.example.whatsappdemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.repo.IncomingMessageRepo;
import com.example.whatsappdemo.repo.MessageRepo;

@Service
public class StatisticsService {

    @Autowired
    private IncomingMessageRepo incomingMessageRepo;

    @Autowired
    private MessageRepo messageRepo;

    public List<MessagesStatisticsDTO> getIncomingMessagesStatistics(){
        return incomingMessageRepo.getMessagesStatistics();
    }

    public List<MessagesStatisticsDTO> getOutgoingMessagesStatisticsDTO(){
        return messageRepo.getMessagesStatistics();
    }

     public List<MessagesStatisticsDTO> getIncomingMessagesStatistics(String phoneNumber){
        return incomingMessageRepo.getMessagesStatistics(phoneNumber);
    }

     public List<MessagesStatisticsDTO> getOutgoingMessagesStatisticsDTO(String phoneNumber){
        return messageRepo.getMessagesStatistics(phoneNumber);
    }
}
