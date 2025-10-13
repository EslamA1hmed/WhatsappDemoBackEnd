package com.example.whatsappdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "all_messages_view")
@Data
public class AllMessagesView {

    @Id
    private Long id;

    private String messageId;
    private String status;
    private String direction; // SENT or RECEIVED
    private String contact;   // الطرف التاني في المحادثة (to أو from)
    private String type;
    private String textBody;

    // Template
    private String templateName;
    private String templateBody;
    private String templateHeader;
    private String templateFooter;

    // Media
    private String mediaId;
    private String mediaUrl;
    private String mimeType;
    private String caption;
    private String filename;

    // Buttons (لو الرسالة فيها أزرار)
    private Long buttonId;
    private String buttonType;
    private String buttonText;
    private String buttonPayload;
    private String buttonUrl;
    private String buttonPhone;

    // Context (لو دي رسالة رد على رسالة تانية)
    private String contextMessageId;
    private String contextFrom;

    private LocalDateTime createdAt;
}
