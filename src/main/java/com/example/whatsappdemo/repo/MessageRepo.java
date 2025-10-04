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

    Page<Message> findAllByOrderByCreatedAtDesc(Pageable pageable);
    @Query("select new com.example.whatsappdemo.dto.MessagesStatisticsDTO(m.status,count(m)) from Message m Group by m.status")
    List<MessagesStatisticsDTO> findStatisticsDTO();

}
