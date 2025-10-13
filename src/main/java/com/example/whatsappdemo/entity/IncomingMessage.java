package com.example.whatsappdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages_incoming",
indexes = {
        @Index(name = "idx_message_message_id", columnList = "messageId")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ الـ ID القادم من WhatsApp (wamid)
    @Column(unique = true)
    private String messageId;
    @Column(name = "`from`")
    private String from;

    @Column(name = "`to`")
    private String to;

    // ✅ رقم البيزنس اللي استقبل الرسالة
    private String toPhoneNumberId;

    // ✅ نوع الرسالة (text, image, document, video, audio, interactive, location, reaction ...)
    private String type;

    // ✅ وقت الاستلام من WhatsApp
    private String timestamp;

    // ✅ تاريخ الإدخال في النظام
    @CreatedDate
    private LocalDateTime receivedAt = LocalDateTime.now();

    // =========================
    // TEXT MESSAGE
    @Column(columnDefinition = "TEXT")
    private String textBody;

    // =========================
    // MEDIA MESSAGE
    private String mediaId;
    private String mimeType;
    private String sha256;
    private String caption;
    private String filename;

    // =========================
    // LOCATION MESSAGE
    private String latitude;
    private String longitude;
    private String address;
    private String locationName;

    // =========================
    // INTERACTIVE MESSAGE
    private String interactiveType;  // button_reply أو list_reply
    private String interactiveId;
    private String interactiveTitle;
    private String interactiveDescription;

    // =========================
    // CONTEXT (لو كانت رد على رسالة سابقة)
    private String contextMessageId;
    private String contextFrom;

    // =========================
    // REACTION
    private String reactionEmoji;
    private String reactionMessageId;
}
