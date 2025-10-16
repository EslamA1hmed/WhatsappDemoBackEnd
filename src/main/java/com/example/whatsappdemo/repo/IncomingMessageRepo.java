package com.example.whatsappdemo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.entity.IncomingMessage;

@Repository
public interface IncomingMessageRepo extends JpaRepository<IncomingMessage, Long> {
    
    Optional<IncomingMessage> findByMessageId(String messageId);

    @Query("SELECT i FROM IncomingMessage i WHERE i.status = 'unread' AND i.from = :phoneNumber")
    List<IncomingMessage> findMessagesByStatus(@Param("phoneNumber") String phoneNumber);


    @Query("select new com.example.whatsappdemo.dto.MessagesStatisticsDTO(i.status,count(i)) from IncomingMessage i Group by i.status")
    List<MessagesStatisticsDTO> getMessagesStatistics();


        @Query("select new com.example.whatsappdemo.dto.MessagesStatisticsDTO(i.status,count(i)) from IncomingMessage i where i.from = :phoneNumber Group by i.status")
    List<MessagesStatisticsDTO> getMessagesStatistics(String phoneNumber);

}
