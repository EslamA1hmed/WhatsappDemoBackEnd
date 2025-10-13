package com.example.whatsappdemo.controller;

import com.example.whatsappdemo.dto.StatusDTO;
import com.example.whatsappdemo.dto.WhatsAppWebhookDTO;
import com.example.whatsappdemo.entity.Message;
import com.example.whatsappdemo.repo.MessageRepo;
import com.example.whatsappdemo.service.IncomingMessageService;
import com.example.whatsappdemo.service.RealTimeNotifierService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageRepo messageRepo;
     @Autowired 
    private RealTimeNotifierService realTimeNotifierService;

    @Autowired 
    private IncomingMessageService incomingMessageService;

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
        if (webhookDTO.getEntry() != null) {
    for (WhatsAppWebhookDTO.Entry entry : webhookDTO.getEntry()) {
        for (WhatsAppWebhookDTO.Change change : entry.getChanges()) {
            WhatsAppWebhookDTO.Value value = change.getValue();

            if (value.getStatuses() != null && !value.getStatuses().isEmpty()) {
                for (WhatsAppWebhookDTO.Status status : value.getStatuses()) {
                    String messageId = status.getId();     // wamid
                    String newStatus = status.getStatus(); // sent, delivered, read, failed

                    int retries = 10; // أقصى عدد محاولات
                    Message message = null;

                    while (retries-- > 0) {
                        Optional<Message> optionalMessage = messageRepo.findByMessageId(messageId);
                        if (optionalMessage.isPresent()) {
                            message = optionalMessage.get();
                            break; // الرسالة لقيتها، اخرج من اللوب
                        } else {
                            try {
                                Thread.sleep(500); // انتظر نصف ثانية قبل المحاولة التالية
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                    if (message != null) {
                        message.setStatus(newStatus);
                        messageRepo.save(message);
                        realTimeNotifierService.notifyMessageStatus(new StatusDTO(newStatus,messageId), message.getTo());
                    } else {
                        System.out.println("⚠️ Message " + messageId + " not found after retries!");
                    }
                }
            }else if(value.getMessages()!=null){
                System.out.println(incomingMessageService.saveMessage(webhookDTO));
            }
        }
    }
}

        System.out.println("📩 Received Webhook Event:");
        System.out.println(objectMapper.writeValueAsString(webhookDTO));
        // مفيش لزوم تبعت حاجة تانية، Spring بيرجع 200 OK تلقائي
    }
}
