package com.example.whatsappdemo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.whatsappdemo.entity.IncomingMessage;

@Repository
public interface IncomingMessageRepo extends JpaRepository<IncomingMessage,Long> {
Optional<IncomingMessage> findByMessageId(String messageId);

}
