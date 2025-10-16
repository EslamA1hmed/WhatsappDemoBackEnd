package com.example.whatsappdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.service.StatisticsService;

@RestController
@RequestMapping("statistics/")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    
    @GetMapping("income/")
    public ResponseEntity<List<MessagesStatisticsDTO>> getIncomingMessagesStatistics(){
        return ResponseEntity.ok().body(statisticsService.getIncomingMessagesStatistics());
    }
    @GetMapping("outgoing/")
    public  ResponseEntity<List<MessagesStatisticsDTO>> getOutgoingMessagesStatisticsDTO(){
        return ResponseEntity.ok().body(statisticsService.getOutgoingMessagesStatisticsDTO());
    }

     @GetMapping("income/{phoneNumber}")
    public ResponseEntity<List<MessagesStatisticsDTO>> getIncomingMessagesStatistics(@PathVariable String phoneNumber){
        return ResponseEntity.ok().body(statisticsService.getIncomingMessagesStatistics(phoneNumber));
    }
    @GetMapping("outgoing/{phoneNumber}")
    public  ResponseEntity<List<MessagesStatisticsDTO>> getOutgoingMessagesStatisticsDTO(@PathVariable String phoneNumber){
        return ResponseEntity.ok().body(statisticsService.getOutgoingMessagesStatisticsDTO(phoneNumber));
    }
}
