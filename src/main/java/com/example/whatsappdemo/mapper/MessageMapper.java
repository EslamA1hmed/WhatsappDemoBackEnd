package com.example.whatsappdemo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.whatsappdemo.dto.MessageResponseDTO;
import com.example.whatsappdemo.dto.WhatsAppMessageDTO;
import com.example.whatsappdemo.dto.WhatsAppTemplatesResponseDTO;
import com.example.whatsappdemo.entity.Message;
import com.example.whatsappdemo.entity.MessageButton;
import com.example.whatsappdemo.entity.MessageContext;
import com.example.whatsappdemo.repo.MessageButtonsRepo;
import com.example.whatsappdemo.service.TemplateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

        @Autowired
        private TemplateService templateService;
        @Autowired
        private MessageButtonsRepo messageButtonsRepo;

        public Message toEntity(WhatsAppMessageDTO dto) {
                Message message = new Message();

                message.setType(dto.getType());
                message.setRecipientType(dto.getRecipient_type());
                message.setTo(dto.getTo());
                if (dto.getContext() != null) {
                        message.setContextMessage(new MessageContext("15", dto.getContext().getMessage_id()));
                }
                message.setFrom(null);

                // ================= TEXT =================
                if ("text".equalsIgnoreCase(dto.getType())) {
                        if (dto.getText() != null) {
                                message.setTextBody(dto.getText().getBody());
                        }
                }

                // ================= IMAGE =================
                else if ("image".equalsIgnoreCase(dto.getType())) {
                        if (dto.getImage() != null) {
                                message.setCaption(dto.getImage().getCaption());
                                message.setMediaId(dto.getImage().getId());
                                message.setMediaUrl(dto.getImage().getLink());
                                if (dto.getImage().getLink() != null || dto.getImage().getId() != null) {
                                        message.setMimeType("image/jpeg");
                                }
                        }
                }

                // ================= TEMPLATE =================
                else if ("template".equalsIgnoreCase(dto.getType()) && dto.getTemplate() != null) {
                        message.setTemplateName(dto.getTemplate().getName());
                        List<MessageButton> buttonEntities = new ArrayList<>();
                        // ✅ هات الـ template الأصلي من Meta
                        WhatsAppTemplatesResponseDTO.TemplateDTO templateFromMeta = templateService
                                        .getTemplateByName(dto.getTemplate().getName());
                        if (templateFromMeta.getCategory().equalsIgnoreCase("AUTHENTICATION")) {
                                String body = dto.getTemplate().getComponents().get(0).getParameters().get(0).getText()
                                                + " is your verification code";
                                if (templateFromMeta.getComponents().get(0).getAddSecurityRecommendation()) {
                                        body += " For your security, do not share this code.";
                                }
                                message.setTemplateBody(body);

                                if (templateFromMeta.getComponents().size() > 1) {

                                        message.setTemplateFooter(
                                                        "This code expires in "
                                                                        + templateFromMeta.getComponents().get(1)
                                                                                        .getCodeExpirationMinutes()
                                                                        + " minutes");
                                }
                                Optional<WhatsAppTemplatesResponseDTO.ComponentDTO> component = templateFromMeta
                                                .getComponents().stream()
                                                .filter(c -> c.getType().equalsIgnoreCase("Buttons")).findFirst();
                                if (component.isPresent()) {
                                        buttonEntities.add(MessageButton.builder()
                                                        .type(component.get().getButtons().get(0).getType())
                                                        .text(component.get().getButtons().get(0).getText()).build());
                                        buttonEntities.forEach(b -> b.setMessage(message));
                                        message.setButtons(buttonEntities);
                                }
                                return message;
                        }

                        // لوب على الـ components من Meta (الأساسية)
                        for (WhatsAppTemplatesResponseDTO.ComponentDTO componentFromMeta : templateFromMeta
                                        .getComponents()) {
                                String type = componentFromMeta.getType().toUpperCase();

                                switch (type) {
                                        case "HEADER":
                                                if ("TEXT".equalsIgnoreCase(componentFromMeta.getFormat())) {
                                                        String headerText = componentFromMeta.getText();
                                                        // شوف لو في parameter جاي من dto
                                                        Optional<WhatsAppMessageDTO.Template.Component> headerComponent = dto
                                                                        .getTemplate().getComponents().stream()
                                                                        .filter(c -> "header"
                                                                                        .equalsIgnoreCase(c.getType()))
                                                                        .findFirst();

                                                        if (headerComponent.isPresent() && headerComponent.get()
                                                                        .getParameters() != null) {
                                                                WhatsAppMessageDTO.Template.Parameter param = headerComponent
                                                                                .get().getParameters().get(0);
                                                                if ("text".equalsIgnoreCase(param.getType())) {
                                                                        headerText = headerText.replace("{{1}}",
                                                                                        param.getText());
                                                                }
                                                        }
                                                        message.setTemplateHeader(headerText);
                                                }
                                                break;

                                        case "BODY":
                                                String bodyText = componentFromMeta.getText();
                                                Optional<WhatsAppMessageDTO.Template.Component> bodyComponent = dto
                                                                .getTemplate().getComponents().stream()
                                                                .filter(c -> "body".equalsIgnoreCase(c.getType()))
                                                                .findFirst();

                                                if (bodyComponent.isPresent()
                                                                && bodyComponent.get().getParameters() != null) {
                                                        int index = 1;
                                                        for (WhatsAppMessageDTO.Template.Parameter param : bodyComponent
                                                                        .get().getParameters()) {
                                                                if ("text".equalsIgnoreCase(param.getType())) {
                                                                        bodyText = bodyText.replace("{{" + index + "}}",
                                                                                        param.getText());
                                                                }
                                                                index++;
                                                        }
                                                }
                                                message.setTemplateBody(bodyText);
                                                break;

                                        case "FOOTER":
                                                message.setTemplateFooter(componentFromMeta.getText());
                                                break;

                                        case "BUTTONS":
                                                componentFromMeta.getButtons().forEach(buttonFromMeta -> {
                                                        String btnType = buttonFromMeta.getType().toUpperCase();
                                                        String buttonText = buttonFromMeta.getText();

                                                        switch (btnType) {
                                                                case "QUICK_REPLY":
                                                                        String payload = null;
                                                                        Optional<WhatsAppMessageDTO.Template.Component> quickReplyComponent = dto
                                                                                        .getTemplate().getComponents()
                                                                                        .stream()
                                                                                        .filter(c -> "button"
                                                                                                        .equalsIgnoreCase(
                                                                                                                        c.getType())
                                                                                                        &&
                                                                                                        "quick_reply".equalsIgnoreCase(
                                                                                                                        c.getSub_type()))
                                                                                        .findFirst();
                                                                        if (quickReplyComponent.isPresent() &&
                                                                                        quickReplyComponent.get()
                                                                                                        .getParameters() != null
                                                                                        &&
                                                                                        !quickReplyComponent.get()
                                                                                                        .getParameters()
                                                                                                        .isEmpty()) {
                                                                                payload = quickReplyComponent.get()
                                                                                                .getParameters().get(0)
                                                                                                .getText();
                                                                        }

                                                                        buttonEntities.add(MessageButton.builder()
                                                                                        .type("QUICK_REPLY")
                                                                                        .text(buttonText)
                                                                                        .payload(payload)
                                                                                        .message(message)
                                                                                        .build());
                                                                        break;

                                                                case "PHONE_NUMBER":
                                                                        String phone = null;
                                                                        Optional<WhatsAppMessageDTO.Template.Component> callComponent = dto
                                                                                        .getTemplate().getComponents()
                                                                                        .stream()
                                                                                        .filter(c -> "button"
                                                                                                        .equalsIgnoreCase(
                                                                                                                        c.getType())
                                                                                                        &&
                                                                                                        "phone_number".equalsIgnoreCase(
                                                                                                                        c.getSub_type()))
                                                                                        .findFirst();
                                                                        if (callComponent.isPresent() &&
                                                                                        callComponent.get()
                                                                                                        .getParameters() != null
                                                                                        &&
                                                                                        !callComponent.get()
                                                                                                        .getParameters()
                                                                                                        .isEmpty()) {
                                                                                phone = callComponent.get()
                                                                                                .getParameters().get(0)
                                                                                                .getText();
                                                                        }

                                                                        buttonEntities.add(MessageButton.builder()
                                                                                        .type("CALL")
                                                                                        .text(buttonText)
                                                                                        .phoneNumber(phone)
                                                                                        .message(message)
                                                                                        .build());
                                                                        break;

                                                                case "URL":
                                                                        String finalUrl = buttonFromMeta.getUrl();
                                                                        Optional<WhatsAppMessageDTO.Template.Component> urlComponent = dto
                                                                                        .getTemplate().getComponents()
                                                                                        .stream()
                                                                                        .filter(c -> "button"
                                                                                                        .equalsIgnoreCase(
                                                                                                                        c.getType())
                                                                                                        &&
                                                                                                        "url".equalsIgnoreCase(
                                                                                                                        c.getSub_type()))
                                                                                        .findFirst();
                                                                        if (urlComponent.isPresent() &&
                                                                                        urlComponent.get()
                                                                                                        .getParameters() != null
                                                                                        &&
                                                                                        !urlComponent.get()
                                                                                                        .getParameters()
                                                                                                        .isEmpty()) {
                                                                                String param = urlComponent.get()
                                                                                                .getParameters().get(0)
                                                                                                .getText();
                                                                                if (param != null && !param.isEmpty()) {
                                                                                        finalUrl = finalUrl.replace(
                                                                                                        "{{1}}", param);
                                                                                }
                                                                        }

                                                                        buttonEntities.add(MessageButton.builder()
                                                                                        .type("URL")
                                                                                        .text(buttonText)
                                                                                        .url(finalUrl)
                                                                                        .message(message)
                                                                                        .build());
                                                                        break;
                                                        }
                                                });
                                                break;
                                }
                        }

                        buttonEntities.forEach(b -> b.setMessage(message));
                        message.setButtons(buttonEntities);
                }
                return message;
        }

        public static MessageResponseDTO fromEntity(Message message) {
                return MessageResponseDTO.builder()
                                .id(message.getId())
                                .direction("SENT")
                                .createdAt(message.getCreatedAt())
                                .messageId(message.getMessageId())
                                .status(message.getStatus())
                                .from(message.getFrom())
                                .to(message.getTo())
                                .type(message.getType())
                                .contextMessageId(message.getContextMessage() != null
                                                ? message.getContextMessage().getContextMessageId()
                                                : null)
                                .contextFrom(message.getContextMessage() != null
                                                ? message.getContextMessage().getContextFrom()
                                                : null)
                                .textBody(message.getTextBody())
                                .templateName(message.getTemplateName())
                                .templateBody(message.getTemplateBody())
                                .templateHeader(message.getTemplateHeader())
                                .templateFooter(message.getTemplateFooter())
                                .mediaId(message.getMediaId())
                                .mediaUrl(message.getMediaUrl())
                                .mimeType(message.getMimeType())
                                .caption(message.getCaption())
                                .filename(message.getFilename())
                                .height(message.getHeight())
                                .width(message.getWidth())
                                .thumbnail(message.getThumbnail())
                                .buttons(
                                                message.getButtons() == null ? null
                                                                : message.getButtons().stream()
                                                                                .map(b -> new MessageResponseDTO.ButtonDTO(
                                                                                                b.getType(),
                                                                                                b.getText(),
                                                                                                b.getPayload(),
                                                                                                b.getUrl(),
                                                                                                b.getPhoneNumber()))
                                                                                .collect(Collectors.toList()))
                                .build();
        }
}
