package com.example.whatsappdemo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.whatsappdemo.dto.MessagesStatisticsDTO;
import com.example.whatsappdemo.entity.Message;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    Optional<Message> findByMessageId(String messageId);

    Page<Message> findByToOrFrom(String to, String from, Pageable pageable);


    Page<Message> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Query("select new com.example.whatsappdemo.dto.MessagesStatisticsDTO(m.status,count(m)) from Message m Group by m.status")
    List<MessagesStatisticsDTO> getMessagesStatistics();

    @Query("select new com.example.whatsappdemo.dto.MessagesStatisticsDTO(m.status,count(m)) from Message m where m.to = :phoneNumber Group by m.status")
    List<MessagesStatisticsDTO> getMessagesStatistics(String phoneNumber);

    // ✅ جلب الرسائل مع رقم معين
    @Query("SELECT m FROM Message m WHERE m.to = :phoneNumber ORDER BY m.createdAt DESC")
    Page<Message> findByPhoneNumber(String phoneNumber, Pageable pageable);

     Optional<Message> findFirstByToOrFromOrderByCreatedAtDesc(String to, String from);

}
