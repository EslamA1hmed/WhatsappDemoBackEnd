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

    // getters / setters

    public static class Entry {
        private String id;
        private List<Change> changes;
        // getters / setters
    }

    public static class Change {
        private String field;
        private Value value;
        // getters / setters
    }

    public static class Value {
        private String messaging_product;
        private Metadata metadata;
        private List<Contact> contacts;
        private List<Message> messages;
        private List<Status> statuses;
        private List<ErrorInfo> errors;

        // قد تضيف حقول إضافية مثل “referral” أو “business_account” أو غيره حسب نوع الحدث
        private Referral referral;
        private BusinessAccountUpdate business_account;
        private TemplateUpdate template_update;
        // getters / setters
    }

    public static class Metadata {
        private String display_phone_number;
        private String phone_number_id;
        // getters / setters
    }

    public static class Contact {
        private Profile profile;
        private String wa_id;
        // getters / setters
    }

    public static class Profile {
        private String name;
        // getters / setters
    }

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
        private Contact contact;  // إذا الرسالة بها جهة اتصال
        private Location location;
        private Interactive interactive;
        private Context context;
        private Reaction reaction;
        private Referral referral;

        // getters / setters
    }

    public static class Text {
        private String body;
        // getters / setters
    }

    public static class Image {
        private String id;
        private String mime_type;
        private String sha256;
        private String caption;
        // getters / setters
    }

    public static class Document {
        private String id;
        private String filename;
        private String mime_type;
        private String caption;
        // getters / setters
    }

    public static class Video {
        private String id;
        private String mime_type;
        private String caption;
        private String sha256;
        // getters / setters
    }

    public static class Audio {
        private String id;
        private String mime_type;
        // getters / setters
    }

    public static class Sticker {
        private String id;
        private String mime_type;
        private String sha256;
        // getters / setters
    }

    public static class Location {
        private String latitude;
        private String longitude;
        private String address;
        private String name;
        // getters / setters
    }

    public static class Interactive {
        private String type;  // e.g. "button_reply", "list_reply"
        private ButtonReply button_reply;
        private ListReply list_reply;
        // getters / setters
    }

    public static class ButtonReply {
        private String id;
        private String title;
        // getters / setters
    }

    public static class ListReply {
        private String id;
        private String title;
        private String description;
        // getters / setters
    }

    public static class Context {
        private String from;
        private String id;
        // getters / setters
    }

    public static class Reaction {
        private String message_id;
        private String emoji;
        // getters / setters
    }

    public static class Referral {
        // بنية Referral متغيرة حسب نوع الإعلان أو الدعوة – استخدم الحقول التي قد تحتاجها
        private String source;
        private String type;
        private String headline;
        private String body;
        private String media_type;
        private String media_url;
        // getters / setters
    }

    public static class Status {
        private String id;
        private String status;
        private String timestamp;
        private String recipient_id;
        private Conversation conversation;
        private Pricing pricing;
        // getters / setters
    }

    public static class Conversation {
        private String id;
        private String origin_type;
        private String expiration;
        // getters / setters
    }

    public static class Pricing {
        private String pricing_model;
        private Double billable;
        private String currency;
        // getters / setters
    }

    public static class ErrorInfo {
        private String code;
        private String title;
        private String details;
        // getters / setters
    }

    public static class BusinessAccountUpdate {
        private String id;
        private String updateEvent;
        private String primaryBusinessLocation;
        private String violationType;
        private List<Restriction> restrictions;
        // getters / setters
    }

    public static class TemplateUpdate {
        // الحقول المناسبة لتحديثات القوالب
        private String name;
        private String language;
        private String status;
        // getters / setters
    }

    public static class Restriction {
        private String restrictionType;
        private String expiration;
        // getters / setters
    }
}
