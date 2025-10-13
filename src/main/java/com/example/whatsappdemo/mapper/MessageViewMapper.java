package com.example.whatsappdemo.mapper;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.entity.AllMessagesView;

import java.util.*;
import java.util.stream.Collectors;

public class MessageViewMapper {

    public static List<MessageResponseDTO> mapList(List<AllMessagesView> viewRows) {

        // Group by messageId علشان الرسالة اللي فيها أكتر من زرار
        Map<String, List<AllMessagesView>> grouped = viewRows.stream()
                .collect(Collectors.groupingBy(AllMessagesView::getMessageId));

        List<MessageResponseDTO> result = new ArrayList<>();

        for (var entry : grouped.entrySet()) {
            var rows = entry.getValue();
            var first = rows.get(0);

            MessageResponseDTO dto = MessageResponseDTO.builder()
                    .id(first.getId())
                    .messageId(first.getMessageId())
                    .status(first.getStatus())
                    .direction(first.getDirection()) // SENT / RECEIVED
                    .to(first.getContact())
                    .type(first.getType())
                    .textBody(first.getTextBody())
                    // TEMPLATE
                    .templateName(first.getTemplateName())
                    .templateBody(first.getTemplateBody())
                    .templateHeader(first.getTemplateHeader())
                    .templateFooter(first.getTemplateFooter())
                    // MEDIA
                    .mediaId(first.getMediaId())
                    .mediaUrl(first.getMediaUrl())
                    .mimeType(first.getMimeType())
                    .caption(first.getCaption())
                    .filename(first.getFilename())
                    .contextMessageId(first.getContextMessageId())
                    .contextFrom(first.getContextFrom())
                    .build();

            // Collect buttons (لو فيه)
            List<MessageResponseDTO.ButtonDTO> buttons = rows.stream()
                    .filter(r -> r.getButtonId() != null)
                    .map(r -> MessageResponseDTO.ButtonDTO.builder()
                            .type(r.getButtonType())
                            .text(r.getButtonText())
                            .payload(r.getButtonPayload())
                            .url(r.getButtonUrl())
                            .phoneNumber(r.getButtonPhone())
                            .build())
                    .toList();

            dto.setButtons(buttons);
            result.add(dto);
        }

        return result;
    }
}
