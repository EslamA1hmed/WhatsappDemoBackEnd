package com.example.whatsappdemo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.whatsappdemo.entity.AllMessages;

@Repository
public interface AllMessagesRepo extends JpaRepository<AllMessages, Long> {

    @Query("SELECT mc FROM AllMessages mc WHERE mc.sender = :phoneNumber OR mc.receiver = :phoneNumber ORDER BY mc.Id DESC")
    Page<AllMessages> findBySenderOrReceiver(String phoneNumber, Pageable pageable);

    @Query("SELECT mc FROM AllMessages mc WHERE mc.sender = :phoneNumber OR mc.receiver = :phoneNumber ORDER BY mc.Id DESC limit 1")
    Optional<AllMessages> findTopBySenderOrReceiverOrderByCreatedAtDesc(String phoneNumber);
}