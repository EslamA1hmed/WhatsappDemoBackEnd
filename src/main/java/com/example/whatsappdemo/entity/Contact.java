package com.example.whatsappdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts",
indexes = {
        @Index(name = "idx_lastMessage", columnList = "lastMessage"),
        @Index(name = "idx_phoneNumber", columnList = "phoneNumber")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime lastMessage;
}