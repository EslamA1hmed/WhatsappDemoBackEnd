package com.example.whatsappdemo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.whatsappdemo.entity.MessageChat;

@Repository
public interface MessageChatRepo extends JpaRepository<MessageChat, Long> {

    @Query("SELECT mc FROM MessageChat mc WHERE mc.sender = :phoneNumber OR mc.receiver = :phoneNumber ORDER BY mc.Id DESC")
    Page<MessageChat> findBySenderOrReceiver(String phoneNumber, Pageable pageable);

    @Query("SELECT mc FROM MessageChat mc WHERE mc.sender = :phoneNumber OR mc.receiver = :phoneNumber ORDER BY mc.Id DESC limit 1")
    Optional<MessageChat> findTopBySenderOrReceiverOrderByCreatedAtDesc(String phoneNumber);
}