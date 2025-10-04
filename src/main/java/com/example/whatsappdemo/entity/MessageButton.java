package com.example.whatsappdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_buttons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageButton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ نوع الزرار: quick_reply, url, call
    private String type;

    // ✅ النص اللي بيظهر على الزرار
    private String text;

    // ✅ القيمة اللي بترجع لو quick_reply
    private String payload;

    // ✅ في حالة زرار URL
    private String url;

    // ✅ في حالة زرار Call
    private String phoneNumber;

    // ✅ كل زرار مرتبط برسالة
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
