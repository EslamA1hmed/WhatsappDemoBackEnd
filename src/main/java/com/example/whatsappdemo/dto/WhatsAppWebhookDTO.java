package com.example.whatsappdemo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WhatsAppWebhookDTO {
    private String object;
    private List<Entry> entry;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        private String id;
        private List<Change> changes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Change {
        private String field;
        private Value value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        private String messaging_product;
        private Metadata metadata;
        private List<Contact> contacts;
        private List<Message> messages;
        private List<Status> statuses;
        private List<ErrorInfo> errors;

        private Referral referral;
        private BusinessAccountUpdate business_account;
        private TemplateUpdate template_update;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String display_phone_number;
        private String phone_number_id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        private Profile profile;
        private String wa_id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String from;
        private String id;
        private String timestamp;
        private String type;

        private Text text;
        private Image image;
        private Document document;
        private Video video;
        private Audio audio;
        private Sticker sticker;
        private Contact contact;
        private Location location;
        private Interactive interactive;
        private Context context;
        private Reaction reaction;
        private Referral referral;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Text {
        private String body;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String id;
        private String mime_type;
        private String sha256;
        private String caption;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Document {
        private String id;
        private String filename;
        private String mime_type;
        private String caption;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Video {
        private String id;
        private String mime_type;
        private String caption;
        private String sha256;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Audio {
        private String id;
        private String mime_type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sticker {
        private String id;
        private String mime_type;
        private String sha256;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        private String latitude;
        private String longitude;
        private String address;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Interactive {
        private String type;
        private ButtonReply button_reply;
        private ListReply list_reply;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ButtonReply {
        private String id;
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListReply {
        private String id;
        private String title;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Context {
        private String from;
        private String id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reaction {
        private String message_id;
        private String emoji;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Referral {
        private String source;
        private String type;
        private String headline;
        private String body;
        private String media_type;
        private String media_url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private String id;
        private String status;
        private String timestamp;
        private String recipient_id;
        private Conversation conversation;
        private Pricing pricing;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Conversation {
        private String id;
        private String origin_type;
        private String expiration;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pricing {
        private String pricing_model;
        private Double billable;
        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorInfo {
        private String code;
        private String title;
        private String details;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessAccountUpdate {
        private String id;
        private String updateEvent;
        private String primaryBusinessLocation;
        private String violationType;
        private List<Restriction> restrictions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateUpdate {
        private String name;
        private String language;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Restriction {
        private String restrictionType;
        private String expiration;
    }
}
