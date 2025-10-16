package com.example.whatsappdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import com.example.whatsappdemo.enums.MessageType;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages_chat",
indexes = {
        @Index(name = "idx_message_message_id", columnList = "messageRefId")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ ده الـ id اللي في الجدول الأصلي (incoming أو outgoing)
    @Column(name = "message_ref_id", nullable = false)
    private String messageRefId;

    // ✅ نحدد نوع الرسالة عشان نعرف نجيبها من انهي جدول
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;


    // ✅ وقت إنشاء الرسالة
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}
