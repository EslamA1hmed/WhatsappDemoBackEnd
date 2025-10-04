package com.example.whatsappdemo.dto;

import lombok.Data;

@Data
public class WhatsAppMediaDTO {
    private String id;
    private String url;
    private String mimeType;
    private Long fileSize;
    private String sha256;
    private String caption;
}
