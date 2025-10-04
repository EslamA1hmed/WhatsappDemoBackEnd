package com.example.whatsappdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {

    private Long id;
    private String messageId;
    private String status;
    private String to;
    private String type;            // text, template, media
    private String textBody;

    // TEMPLATE fields
    private String templateName;
    private String templateBody;
    private String templateHeader;
    private String templateFooter;

    // MEDIA fields
    private String mediaId;
    private String mimeType;
    private String mediaUrl;
    private String caption;
    private String filename;

    // BUTTONS
    private List<ButtonDTO> buttons;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ButtonDTO {
        private String type;       // quick_reply, url, call
        private String text;       // نص الزرار
        private String payload;    // القيمة لو quick_reply
        private String url;        // لو زرار URL
        private String phoneNumber;// لو زرار Call
    }
}
