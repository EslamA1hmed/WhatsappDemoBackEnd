package com.example.whatsappdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "messages_outgoing",
indexes = {
        @Index(name = "idx_message_message_id", columnList = "messageId")
        ,@Index(name = "idx_to", columnList = "`to`")
    })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Wamid اللي بيرجعه Meta بعد الإرسال
    @Column(unique = true)
    private String messageId;
    private String status = "Not delverid";

    private String messagingProduct = "whatsapp";

    // ✅ رقم المستقبل
    @Column(name = "`to`")
    private String to;
     @Column(name = "`from`")
    private String from;


    private String recipientType = "individual";

    // ✅ نوع الرسالة: text, template, image, video, document, audio...
    private String type;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    // =======================
    // TEXT MESSAGE
    @Column(columnDefinition = "TEXT")
    private String textBody;

    // =======================
    // TEMPLATE MESSAGE
    private String templateName;

    @Column(columnDefinition = "TEXT")
    private String templateBody;

    private String templateHeader;
    private String templateFooter;
    private String templateHeaderMediaId;
    private String templateHeaderMimeType;
    private String templateHeaderFilename;

    // =======================
    // MEDIA MESSAGE
    private String mediaId;
    private String mediaUrl;
    private String mimeType;
    private String caption;
    private String filename;
    @Column(columnDefinition = "TEXT")
    private String thumbnail; // لتخزين Base64 string

    private Integer width;

    private Integer height;

    // =======================
    // BUTTONS
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageButton> buttons;

    // =======================
    // CONTEXT (reply to another outgoing msg if needed)
    @Embedded
    private MessageContext contextMessage;
}
