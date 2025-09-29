package com.example.whatsappdemo.controller;

import com.example.whatsappdemo.dto.WhatsAppWebhookDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ GET: Verification Step
    @GetMapping
    public String verifyWebhook(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.challenge", required = false) String challenge,
            @RequestParam(name = "hub.verify_token", required = false) String verifyToken) {

        // نفس التوكن اللي انت ضايفه في إعدادات Meta
        String myVerifyToken = "my_secret_token";

        if ("subscribe".equals(mode) && myVerifyToken.equals(verifyToken)) {
            System.out.println("✅ Webhook verified successfully!");
            return challenge; // لازم ترجع الـ challenge عشان يتقبل
        } else {
            System.out.println("❌ Verification failed!");
            return "Verification failed!";
        }
    }

    // ✅ POST: Receive Webhook Events
    @PostMapping
    public void receiveWebhook(@RequestBody WhatsAppWebhookDTO webhookDTO) throws JsonProcessingException {
        System.out.println("📩 Received Webhook Event:");
        System.out.println(objectMapper.writeValueAsString(webhookDTO));
        // مفيش لزوم تبعت حاجة تانية، Spring بيرجع 200 OK تلقائي
    }
}
