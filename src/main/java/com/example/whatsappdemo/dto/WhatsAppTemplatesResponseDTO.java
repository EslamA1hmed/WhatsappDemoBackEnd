package com.example.whatsappdemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WhatsAppTemplatesResponseDTO {

    private List<TemplateDTO> data;
    private PagingDTO paging;

    // ---------------- Sub-Classes ---------------- //

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateDTO {
        private String id;
        private String name;
        @JsonProperty("parameter_format")
        private String parameterFormat; // POSITIONAL, NAMED
        private String language;
        private String status;
        private String category;
        private List<ComponentDTO> components;
        @JsonProperty("message_send_ttl_seconds")
        private Integer messageSendTtlSeconds; // إضافة
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentDTO {
        private String type; // BODY, HEADER, FOOTER, BUTTONS
        private String format; // TEXT, IMAGE, VIDEO... (can be null for BODY/FOOTER in AUTHENTICATION)
        private String text;
        private List<ButtonDTO> buttons;
        private TemplateExampleDTO example;
        @JsonProperty("add_security_recommendation")
        private Boolean addSecurityRecommendation; // لـ BODY
        @JsonProperty("code_expiration_minutes")
        private Integer codeExpirationMinutes; // لـ FOOTER
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ButtonDTO {
        private String type; // QUICK_REPLY, URL, PHONE_NUMBER, CATALOG, OTP
        private String text;
        private String url; // لـ URL و OTP buttons
        private List<String> example; // لـ example URLs
        @JsonProperty("otp_type")
        private String otpType; // COPY_CODE, ONE_TAP
        @JsonProperty("autofill_text")
        private String autofillText; // لـ ONE_TAP
        @JsonProperty("package_name")
        private String packageName;
        @JsonProperty("phone_number") // لـ ONE_TAP
        private String phoneNumber;
        @JsonProperty("signature_hash")
        private String signatureHash; // لـ ONE_TAP
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateExampleDTO {
        @JsonProperty("header_text")
        private List<String> headerTexts;
        @JsonProperty("body_text")
        private List<List<String>> bodyTexts;
        @JsonProperty("header_handle")
        private List<String> headerHandles;
        private List<String> example; // لـ button examples
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagingDTO {
        private CursorsDTO cursors;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CursorsDTO {
        private String before;
        private String after;
    }
}