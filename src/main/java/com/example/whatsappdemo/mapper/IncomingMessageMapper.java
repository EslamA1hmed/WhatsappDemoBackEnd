package com.example.whatsappdemo.mapper;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.dto.WhatsAppWebhookDTO;
import com.example.whatsappdemo.entity.IncomingMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IncomingMessageMapper {

    public List<IncomingMessage> toEntities(WhatsAppWebhookDTO dto) {
        List<IncomingMessage> incomingMessages = new ArrayList<>();

        if (dto == null || dto.getEntry() == null) return incomingMessages;

        dto.getEntry().forEach(entry -> {
            entry.getChanges().forEach(change -> {
                var value = change.getValue();
                if (value == null || value.getMessages() == null) return;

                value.getMessages().forEach(msg -> {
                    IncomingMessage incoming = new IncomingMessage();

                    incoming.setMessageId(msg.getId());
                    incoming.setFrom(msg.getFrom());
                    incoming.setToPhoneNumberId(value.getMetadata() != null ? value.getMetadata().getPhone_number_id() : null);
                    incoming.setTimestamp(msg.getTimestamp());
                    incoming.setType(msg.getType());
                    incoming.setTo(value.getMetadata().getDisplay_phone_number());

                    switch (msg.getType()) {
                        case "text" -> mapTextMessage(msg, incoming);
                        case "image" -> mapImageMessage(msg, incoming);
                        case "document" -> mapDocumentMessage(msg, incoming);
                        case "video" -> mapVideoMessage(msg, incoming);
                        case "location" -> mapLocationMessage(msg, incoming);
                        case "interactive" -> mapInteractiveMessage(msg, incoming);
                        case "reaction" -> mapReactionMessage(msg, incoming);
                    }

                    // ✅ لو الرسالة دي رد على رسالة سابقة (reply)
                    if (msg.getContext() != null) {
                        incoming.setContextMessageId(msg.getContext().getId());
                        incoming.setContextFrom(msg.getContext().getFrom());
                    }

                    incomingMessages.add(incoming);
                });
            });
        });

        return incomingMessages;
    }

    private void mapTextMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        if (msg.getText() != null)
            incoming.setTextBody(msg.getText().getBody());
    }

    private void mapImageMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        if (msg.getImage() != null) {
            incoming.setMediaId(msg.getImage().getId());
            incoming.setMimeType(msg.getImage().getMime_type());
            incoming.setSha256(msg.getImage().getSha256());
            incoming.setCaption(msg.getImage().getCaption());
        }
    }

    private void mapDocumentMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        if (msg.getDocument() != null) {
            incoming.setMediaId(msg.getDocument().getId());
            incoming.setFilename(msg.getDocument().getFilename());
            incoming.setMimeType(msg.getDocument().getMime_type());
            incoming.setCaption(msg.getDocument().getCaption());
        }
    }

    private void mapVideoMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        if (msg.getVideo() != null) {
            incoming.setMediaId(msg.getVideo().getId());
            incoming.setMimeType(msg.getVideo().getMime_type());
            incoming.setCaption(msg.getVideo().getCaption());
        }
    }

    private void mapLocationMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        if (msg.getLocation() != null) {
            incoming.setLatitude(msg.getLocation().getLatitude());
            incoming.setLongitude(msg.getLocation().getLongitude());
            incoming.setAddress(msg.getLocation().getAddress());
            incoming.setLocationName(msg.getLocation().getName());
        }
    }

    private void mapInteractiveMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        var interactive = msg.getInteractive();
        if (interactive == null) return;

        incoming.setInteractiveType(interactive.getType());
        if (interactive.getButton_reply() != null) {
            incoming.setInteractiveId(interactive.getButton_reply().getId());
            incoming.setInteractiveTitle(interactive.getButton_reply().getTitle());
        } else if (interactive.getList_reply() != null) {
            incoming.setInteractiveId(interactive.getList_reply().getId());
            incoming.setInteractiveTitle(interactive.getList_reply().getTitle());
            incoming.setInteractiveDescription(interactive.getList_reply().getDescription());
        }
    }

    private void mapReactionMessage(WhatsAppWebhookDTO.Message msg, IncomingMessage incoming) {
        if (msg.getReaction() != null) {
            incoming.setReactionEmoji(msg.getReaction().getEmoji());
            incoming.setReactionMessageId(msg.getReaction().getMessage_id());
        }
    }

    public MessageResponseDTO toDTO(IncomingMessage entity) {
        if (entity == null) return null;

        return MessageResponseDTO.builder()
                .id(entity.getId())
                .createdAt(entity.getReceivedAt())
                .messageId(entity.getMessageId())
                .direction("RECEIVED") // لأنها رسالة جاية من العميل
                .from(entity.getFrom())
                .to(entity.getTo()) // لو ضفت عمود to زي ما عامل فوق
                .type(entity.getType())
                .textBody(entity.getTextBody())
                .mediaId(entity.getMediaId())
                .mimeType(entity.getMimeType())
                .caption(entity.getCaption())
                .filename(entity.getFilename())
                .contextMessageId(entity.getContextMessageId())
                .contextFrom(entity.getContextFrom())
                .build();
    }
}
